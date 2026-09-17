package com.trading.riskengine;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.trading.riskengine.application.KillSwitchPolicy;
import com.trading.riskengine.application.RiskEngineShard;
import com.trading.riskengine.config.ResilienceConfig;
import com.trading.riskengine.infrastructure.MarketDataFeedHandler;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.micrometer.CircuitBreakerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final int RING_BUFFER_SIZE;
    private static final int DISRUPTOR_THREADS;
    private static final int HTTP_PORT;
    private static final int GRPC_PORT;
    private static final boolean ZGC_ENABLED;
    private static final long ZGC_MAX_PAUSE_MS;

    private final Map<String, RiskEngineShard> shards;
    private final Disruptor<RiskOrderEvent> riskDisruptor;
    private final ExecutorService complianceQueryExecutor;
    private final KillSwitchPolicy killSwitchPolicy;
    private final CircuitBreaker marketDataCircuitBreaker;
    private volatile boolean running;

    static {
        Properties props = new Properties();
        try (var input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }

        RING_BUFFER_SIZE = Integer.parseInt(props.getProperty("risk.engine.ringbuffer.size", "65536"));
        DISRUPTOR_THREADS = Integer.parseInt(props.getProperty("risk.engine.disruptor.threads", "4"));
        HTTP_PORT = Integer.parseInt(props.getProperty("risk.engine.http.port", "8080"));
        GRPC_PORT = Integer.parseInt(props.getProperty("risk.engine.grpc.port", "9090"));
        ZGC_ENABLED = Boolean.parseBoolean(props.getProperty("risk.engine.zgc.enabled", "true"));
        ZGC_MAX_PAUSE_MS = Long.parseLong(props.getProperty("risk.engine.zgc.maxPauseMs", "10"));
    }

    public Main() {
        this.shards = new ConcurrentHashMap<>();
        this.complianceQueryExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.killSwitchPolicy = new KillSwitchPolicy();
        this.marketDataCircuitBreaker = ResilienceConfig.createDynamicCircuitBreaker("marketData", 0.5, 100);

        this.riskDisruptor = new Disruptor<>(
            RiskOrderEvent::new,
            RING_BUFFER_SIZE,
            Executors.newFixedThreadPool(DISRUPTOR_THREADS, r -> {
                Thread t = new Thread(r, "risk-disruptor-" + ThreadLocalRandom.current().nextInt(1000));
                t.setPriority(Thread.MAX_PRIORITY);
                t.setDaemon(false);
                return t;
            }),
            ProducerType.MULTI,
            new BlockingWaitStrategy()
        );

        configureDisruptorHandlers();
    }

    private void configureDisruptorHandlers() {
        riskDisruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            String instrument = event.instrument();
            RiskEngineShard shard = shards.get(instrument);

            if (shard == null) {
                logger.warn("No shard found for instrument: {}", instrument);
                return;
            }

            if (killSwitchPolicy.isTradingHalted(event.traderId())) {
                logger.warn("Trading halted for trader: {} - order rejected", event.traderId());
                event.setRejected(true);
                event.setRejectionReason("KILL_SWITCH_ACTIVE");
                return;
            }

            RiskEvaluationResult result = shard.evaluateOrder(
                event.orderId(),
                event.traderId(),
                event.strategyId(),
                event.instrument(),
                event.quantity(),
                event.price(),
                event.side()
            );

            event.setAccepted(result.isAccepted());
            event.setRiskScore(result.riskScore());
            event.setVaR(result.currentVaR());
            event.setRejectionReason(result.rejectionReason());

            if (!result.isAccepted()) {
                logger.warn("Order rejected: {} - reason: {}", event.orderId(), result.rejectionReason());
            }
        });

        riskDisruptor.setDefaultExceptionHandler(new DisruptorExceptionHandler());
    }

    public void initialize() {
        logger.info("Initializing Risk Engine with RingBuffer size: {}, Disruptor threads: {}",
            RING_BUFFER_SIZE, DISRUPTOR_THREADS);

        if (ZGC_ENABLED) {
            logger.info("ZGC enabled with max pause: {}ms", ZGC_MAX_PAUSE_MS);
        }

        initializeInstrumentShards();
        riskDisruptor.start();

        logger.info("Risk Engine started successfully");
        logger.info("Compliance HTTP endpoint: http://localhost:{}/health", HTTP_PORT);
        logger.info("Compliance gRPC endpoint: localhost:{}", GRPC_PORT);
    }

    private void initializeInstrumentShards() {
        String[] instruments = {"AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "NVDA", "META", "NFLX"};

        for (String instrument : instruments) {
            RiskEngineShard shard = new RiskEngineShard(
                instrument,
                marketDataCircuitBreaker,
                killSwitchPolicy
            );
            shards.put(instrument, shard);
            logger.info("Initialized shard for instrument: {}", instrument);
        }
    }

    public void startComplianceServer() {
        complianceQueryExecutor.submit(() -> {
            try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
                serverSocket.bind(new InetSocketAddress(HTTP_PORT));
                serverSocket.configureBlocking(false);
                logger.info("Compliance HTTP server started on port {}", HTTP_PORT);

                while (running) {
                    SocketChannel client = serverSocket.accept();
                    if (client != null) {
                        handleComplianceQuery(client);
                    }
                    Thread.sleep(1);
                }
            } catch (IOException e) {
                logger.error("Compliance server error", e);
            }
        });
    }

    private void handleComplianceQuery(SocketChannel client) {
        complianceQueryExecutor.submit(() -> {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(8192);
                client.read(buffer);
                buffer.flip();

                String request = StandardCharsets.UTF_8.decode(buffer).toString();

                if (request.contains("/health")) {
                    String response = buildHealthResponse();
                    client.write(StandardCharsets.UTF_8.encode(response));
                } else if (request.contains("/risk exposure")) {
                    String response = buildExposureResponse();
                    client.write(StandardCharsets.UTF_8.encode(response));
                } else if (request.contains("/var")) {
                    String response = buildVaRResponse();
                    client.write(StandardCharsets.UTF_8.encode(response));
                }

                client.close();
            } catch (IOException e) {
                logger.debug("Error handling compliance query", e);
            }
        });
    }

    private String buildHealthResponse() {
        return "HTTP/1.1 200 OK\r\n" +
               "Content-Type: application/json\r\n" +
               "X-MiFID-II-Trace-ID: " + java.util.UUID.randomUUID() + "\r\n" +
               "\r\n" +
               "{\"status\":\"UP\",\"timestamp\":\"" + Instant.now() + "\",\"shards\":" + shards.size() + "}";
    }

    private String buildExposureResponse() {
        StringBuilder sb = new StringBuilder("{\"exposures\":[");
        shards.forEach((instrument, shard) -> {
            sb.append("{\"instrument\":\"")
              .append(instrument)
              .append("\",\"exposure\":")
              .append(shard.getCurrentExposure())
              .append("},");
        });
        sb.append("]}");
        return "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + sb;
    }

    private String buildVaRResponse() {
        double totalVaR = shards.values().stream()
            .mapToDouble(RiskEngineShard::getCurrentVaR)
            .sum();
        return "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" +
               "{\"totalVaR\":" + totalVaR + ",\"confidenceLevel\":0.99,\"horizon\":\"1D\"}";
    }

    public void submitOrder(String orderId, String traderId, String strategyId,
                           String instrument, long quantity, double price, String side) {
        RingBuffer<RiskOrderEvent> ringBuffer = riskDisruptor.getRingBuffer();

        long sequence = ringBuffer.next();
        try {
            RiskOrderEvent event = ringBuffer.get(sequence);
            event.setOrderId(orderId);
            event.setTraderId(traderId);
            event.setStrategyId(strategyId);
            event.setInstrument(instrument);
            event.setQuantity(quantity);
            event.setPrice(price);
            event.setSide(side);
            event.setTimestamp(Instant.now());
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void shutdown() {
        logger.info("Shutting down Risk Engine...");
        running = false;

        riskDisruptor.shutdown(10, TimeUnit.SECONDS);
        complianceQueryExecutor.shutdown();

        shards.values().forEach(RiskEngineShard::shutdown);

        logger.info("Risk Engine shutdown complete");
    }

    public static void main(String[] args) throws Exception {
        Main engine = new Main();
        engine.initialize();
        engine.startComplianceServer();

        Runtime.getRuntime().addShutdownHook(new Thread(engine::shutdown));

        logger.info("Risk Engine is running. Press Ctrl+C to stop.");

        Thread.currentThread().join();
    }

    public record RiskOrderEvent() {
        private String orderId;
        private String traderId;
        private String strategyId;
        private String instrument;
        private long quantity;
        private double price;
        private String side;
        private Instant timestamp;
        private boolean accepted;
        private boolean rejected;
        private double riskScore;
        private double var;
        private String rejectionReason;

        public void setOrderId(String orderId) { this.orderId = orderId; }
        public void setTraderId(String traderId) { this.traderId = traderId; }
        public void setStrategyId(String strategyId) { this.strategyId = strategyId; }
        public void setInstrument(String instrument) { this.instrument = instrument; }
        public void setQuantity(long quantity) { this.quantity = quantity; }
        public void setPrice(double price) { this.price = price; }
        public void setSide(String side) { this.side = side; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public void setAccepted(boolean accepted) { this.accepted = accepted; }
        public void setRejected(boolean rejected) { this.rejected = rejected; }
        public void setRiskScore(double riskScore) { this.riskScore = riskScore; }
        public void setVaR(double var) { this.var = var; }
        public void setRejectionReason(String reason) { this.rejectionReason = reason; }

        public String orderId() { return orderId; }
        public String traderId() { return traderId; }
        public String strategyId() { return strategyId; }
        public String instrument() { return instrument; }
        public long quantity() { return quantity; }
        public double price() { return price; }
        public String side() { return side; }
        public Instant timestamp() { return timestamp; }
        public boolean isAccepted() { return accepted; }
        public boolean isRejected() { return rejected; }
        public double riskScore() { return riskScore; }
        public double var() { return var; }
        public String rejectionReason() { return rejectionReason; }
    }

    public record RiskEvaluationResult(
        boolean isAccepted,
        double riskScore,
        double currentVaR,
        String rejectionReason
    ) {
        public static RiskEvaluationResult accepted(double riskScore, double var) {
            return new RiskEvaluationResult(true, riskScore, var, null);
        }

        public static RiskEvaluationResult rejected(double riskScore, double var, String reason) {
            return new RiskEvaluationResult(false, riskScore, var, reason);
        }
    }

    private static class DisruptorExceptionHandler implements com.lmax.disruptor.ExceptionHandler<Object> {
        private static final Logger log = LogManager.getLogger(DisruptorExceptionHandler.class);

        @Override
        public void handleEventException(Throwable ex, long sequence, Object event) {
            log.error("Exception processing event at sequence: {}", sequence, ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            log.error("Exception starting disruptor", ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            log.error("Exception shutting down disruptor", ex);
        }
    }
}