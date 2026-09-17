package com.trading.riskengine.application;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.RiskOrder.RiskStatus;
import com.trading.riskengine.domain.VaRModel;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RiskEngineShard {
    private static final Logger logger = LogManager.getLogger(RiskEngineShard.class);
    private static final int RING_BUFFER_SIZE = 8192;
    private static final double DEFAULT_CONFIDENCE_LEVEL = 0.99;
    private static final int DEFAULT_LOOKBACK = 252;
    private static final double DEFAULT_DECAY_FACTOR = 0.94;
    private static final double VOLATILITY_SCALING_FACTOR = 2.5;
    private static final int CIRCUIT_BREAKER_WINDOW_SECONDS = 60;
    private static final int CIRCUIT_BREAKER_MIN_CALLS = 10;
    private static final double CIRCUIT_BREAKER_FAILURE_RATE = 0.5;

    private final String instrument;
    private final VaRModel varModel;
    private final CircuitBreaker circuitBreaker;
    private final Disruptor<RiskEvent> disruptor;
    private final AtomicBoolean enabled;
    private final AtomicReference<BigDecimal> positionLimit;
    private final AtomicReference<BigDecimal> currentExposure;
    private final Map<String, RiskOrder> recentOrders;
    private final ExecutorService queryExecutor;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public RiskEngineShard(String instrument, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.instrument = instrument;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.varModel = new VaRModel(instrument, DEFAULT_LOOKBACK, DEFAULT_CONFIDENCE_LEVEL, DEFAULT_DECAY_FACTOR);
        this.circuitBreaker = createCircuitBreaker(instrument);
        this.disruptor = createDisruptor();
        this.enabled = new AtomicBoolean(true);
        this.positionLimit = new AtomicReference<>(new BigDecimal("1000000"));
        this.currentExposure = new AtomicReference<>(BigDecimal.ZERO);
        this.recentOrders = new ConcurrentHashMap<>();
        this.queryExecutor = Executors.newVirtualThreadPerTaskExecutor();
        
        logger.info("RiskEngineShard initialized for instrument: {} with limit: {}", 
            instrument, positionLimit.get());
    }

    private CircuitBreaker createCircuitBreaker(String instrument) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .slidingWindowSize(CIRCUIT_BREAKER_WINDOW_SECONDS)
            .minimumNumberOfCalls(CIRCUIT_BREAKER_MIN_CALLS)
            .failureRateThreshold(CIRCUIT_BREAKER_FAILURE_RATE)
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .permittedNumberOfCallsInHalfOpenState(3)
            .slowCallRateThreshold(80.0)
            .slowCallDurationThreshold(Duration.ofSeconds(2))
            .build();
        
        CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker("shard-" + instrument);
        logger.info("Circuit breaker created for shard: {}", instrument);
        return cb;
    }

    private Disruptor<RiskEvent> createDisruptor() {
        Disruptor<RiskEvent> disruptor = new Disruptor<>(
            RiskEvent::new,
            RING_BUFFER_SIZE,
            r -> {
                Thread t = new Thread(r, "shard-" + instrument);
                t.setDaemon(true);
                return t;
            },
            ProducerType.SINGLE,
            new LiteTimeoutWaitStrategy(10, TimeUnit.MILLISECONDS)
        );
        
        disruptor.handleEventsWith(new RiskEventHandler());
        disruptor.setDefaultExceptionHandler(new ShardExceptionHandler());
        disruptor.start();
        
        return disruptor;
    }

    public RiskOrder evaluateOrder(RiskOrder order) {
        if (!enabled.get()) {
            return order.withStatus(RiskStatus.REJECTED, "Shard disabled for " + instrument);
        }

        if (circuitBreaker.getState() == CircuitBreaker.State.OPEN) {
            return order.withStatus(RiskStatus.REJECTED, "Circuit breaker open for " + instrument);
        }

        return circuitBreaker.executeSupplier(() -> {
            BigDecimal var = varModel.getVaR();
            BigDecimal orderValue = order.quantity().multiply(order.price());
            BigDecimal totalExposure = currentExposure.get().add(orderValue);
            
            double volatility = varModel.getVolatility().doubleValue();
            BigDecimal dynamicLimit = positionLimit.get()
                .multiply(BigDecimal.valueOf(VOLATILITY_SCALING_FACTOR / (1 + volatility)));
            
            if (orderValue.compareTo(dynamicLimit) > 0) {
                return order.withStatus(RiskStatus.REJECTED, 
                    "Order value " + orderValue + " exceeds dynamic limit " + dynamicLimit);
            }
            
            if (varModel.isBreached(totalExposure)) {
                return order.withStatus(RiskStatus.REJECTED, 
                    "VaR breach: exposure " + totalExposure + " exceeds VaR " + var);
            }
            
            if (order.exceedsTraderLimit() || order.exceedsStrategyLimit() || 
                order.exceedsInstrumentLimit() || order.exceedsVaR()) {
                return order.withStatus(RiskStatus.REJECTED, "Limit breach detected");
            }
            
            RiskOrder approved = order.withStatus(RiskStatus.APPROVED, "Approved for " + instrument);
            recentOrders.put(order.orderId(), approved);
            currentExposure.updateAndGet(current -> current.add(orderValue));
            
            return approved;
        });
    }

    public void submitOrder(RiskOrder order) {
        long sequence = disruptor.getRingBuffer().next();
        try {
            RiskEvent event = disruptor.getRingBuffer().get(sequence);
            event.setOrder(order);
            event.setSubmitTime(Instant.now());
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public VaRModel getVaRModel() {
        return varModel;
    }

    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }

    public BigDecimal getPositionLimit() {
        return positionLimit.get();
    }

    public void setPositionLimit(BigDecimal newLimit) {
        positionLimit.set(newLimit);
        logger.info("Position limit updated for {}: {}", instrument, newLimit);
    }

    public BigDecimal getCurrentExposure() {
        return currentExposure.get();
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void enable() {
        enabled.set(true);
        logger.info("Shard enabled: {}", instrument);
    }

    public void disable() {
        enabled.set(false);
        logger.warn("Shard disabled: {}", instrument);
    }

    public Map<String, RiskOrder> getRecentOrders() {
        return Map.copyOf(recentOrders);
    }

    public void updateVolatilityCalibration(double newVolatilityMultiplier) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .slidingWindowSize(CIRCUIT_BREAKER_WINDOW_SECONDS)
            .minimumNumberOfCalls(CIRCUIT_BREAKER_MIN_CALLS)
            .failureRateThreshold(CIRCUIT_BREAKER_FAILURE_RATE * newVolatilityMultiplier)
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .permittedNumberOfCallsInHalfOpenState(3)
            .build();
        
        circuitBreaker.resetTransitionHistory();
        logger.info("Circuit breaker recalibrated for {} with failure rate: {}", 
            instrument, CIRCUIT_BREAKER_FAILURE_RATE * newVolatilityMultiplier);
    }

    public void shutdown() {
        disruptor.halt();
        queryExecutor.shutdown();
        try {
            if (!queryExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                queryExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            queryExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("Shard shutdown complete: {}", instrument);
    }

    private class RiskEventHandler implements EventHandler<RiskEvent> {
        @Override
        public void onEvent(RiskEvent event, long sequence, boolean endOfBatch) {
            try {
                evaluateOrder(event.getOrder());
            } catch (Exception e) {
                logger.error("Error evaluating order in shard {}: {}", instrument, e.getMessage(), e);
            }
        }
    }

    private static class ShardExceptionHandler implements ExceptionHandler<RiskEvent> {
        private static final Logger errorLogger = LogManager.getLogger("ShardError");

        @Override
        public void handleEventException(Throwable ex, long sequence, RiskEvent event) {
            errorLogger.error("Shard exception: {}", ex.getMessage(), ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            errorLogger.error("Shard start exception: {}", ex.getMessage(), ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            errorLogger.error("Shard shutdown exception: {}", ex.getMessage(), ex);
        }
    }

    public static class RiskEvent {
        private RiskOrder order;
        private Instant submitTime;

        public RiskOrder getOrder() { return order; }
        public void setOrder(RiskOrder order) { this.order = order; }
        public Instant getSubmitTime() { return submitTime; }
        public void setSubmitTime(Instant submitTime) { this.submitTime = submitTime; }
    }

    private static class LiteTimeoutWaitStrategy implements WaitStrategy {
        private final long timeout;
        private final TimeUnit unit;

        LiteTimeoutWaitStrategy(long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.unit = unit;
        }

        @Override
        public long waitFor(long sequence, SequenceCursor cursor, Sequence dependentSequence, 
                           Callback callback) throws InterruptedException {
            long availableSequence;
            while ((availableSequence = cursor.getCurrentSequence()) < sequence) {
                callback.waitFor(sequence);
                if (unit.timedWait(Thread.currentThread(), timeout) == 0) {
                    return cursor.getCurrentSequence();
                }
            }
            return availableSequence;
        }

        @Override
        public void signalAllWhenBlocking() {
        }
    }
}