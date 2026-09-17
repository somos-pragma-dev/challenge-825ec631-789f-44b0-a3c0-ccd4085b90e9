package com.trading.riskengine.infrastructure;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.trading.riskengine.application.RiskEngineShard;
import com.trading.riskengine.domain.VaRModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarketDataFeedHandler {
    private static final Logger logger = LogManager.getLogger(MarketDataFeedHandler.class);
    private static final int RING_BUFFER_SIZE = 16384;
    private static final int THREAD_COUNT = 4;
    private static final long FEED_TIMEOUT_MS = 100;
    private static final double MIN_VOLUME = 0.001;
    private static final int MAX_ORDERBOOK_DEPTH = 20;

    private final Map<String, RiskEngineShard> instrumentShards;
    private final Map<String, Disruptor<MarketDataEvent>> disruptorsByInstrument;
    private final ExecutorService feedExecutor;
    private final AtomicBoolean running;
    private final Map<String, Long> lastUpdateTimestamps;
    private final Map<String, Integer> sequenceNumbers;

    public MarketDataFeedHandler(Map<String, RiskEngineShard> instrumentShards) {
        this.instrumentShards = instrumentShards;
        this.disruptorsByInstrument = new ConcurrentHashMap<>();
        this.feedExecutor = Executors.newFixedThreadPool(THREAD_COUNT, r -> {
            Thread t = new Thread(r, "market-data-feed");
            t.setDaemon(true);
            return t;
        });
        this.running = new AtomicBoolean(false);
        this.lastUpdateTimestamps = new ConcurrentHashMap<>();
        this.sequenceNumbers = new ConcurrentHashMap<>();
        initializeDisruptors();
    }

    private void initializeDisruptors() {
        for (String instrument : instrumentShards.keySet()) {
            Disruptor<MarketDataEvent> disruptor = new Disruptor<>(
                MarketDataEvent::new,
                RING_BUFFER_SIZE,
                r -> {
                    Thread t = new Thread(r, "disruptor-" + instrument);
                    t.setDaemon(true);
                    return t;
                },
                ProducerType.MULTI,
                new BlockingWaitStrategy()
            );
            
            disruptor.handleEventsWith(new MarketDataEventHandler(instrument));
            disruptor.setDefaultExceptionHandler(new DisruptorErrorHandler(instrument));
            disruptor.start();
            disruptorsByInstrument.put(instrument, disruptor);
            logger.info("Disruptor initialized for instrument: {}", instrument);
        }
    }

    public void onOrderBookUpdate(String instrument, double[] bidPrices, double[] bidVolumes,
                                   double[] askPrices, double[] askVolumes) {
        if (!running.get()) {
            return;
        }
        
        Disruptor<MarketDataEvent> disruptor = disruptorsByInstrument.get(instrument);
        if (disruptor == null) {
            logger.warn("No disruptor found for instrument: {}", instrument);
            return;
        }

        long sequence = disruptor.getRingBuffer().next();
        try {
            MarketDataEvent event = disruptor.getRingBuffer().get(sequence);
            event.setInstrument(instrument);
            event.setEventType(MarketDataEvent.EventType.ORDERBOOK);
            event.setBidPrices(bidPrices.clone());
            event.setBidVolumes(bidVolumes.clone());
            event.setAskPrices(askPrices.clone());
            event.setAskVolumes(askVolumes.clone());
            event.setTimestamp(Instant.now());
            event.setSequenceNumber(sequenceNumbers.compute(instrument, (k, v) -> v == null ? 1 : v + 1));
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public void onTrade(String instrument, double price, double volume, Instant timestamp) {
        if (!running.get() || volume < MIN_VOLUME) {
            return;
        }

        Disruptor<MarketDataEvent> disruptor = disruptorsByInstrument.get(instrument);
        if (disruptor == null) {
            return;
        }

        long sequence = disruptor.getRingBuffer().next();
        try {
            MarketDataEvent event = disruptor.getRingBuffer().get(sequence);
            event.setInstrument(instrument);
            event.setEventType(MarketDataEvent.EventType.TRADE);
            event.setTradePrice(price);
            event.setTradeVolume(volume);
            event.setTimestamp(timestamp);
            event.setSequenceNumber(sequenceNumbers.compute(instrument, (k, v) -> v == null ? 1 : v + 1));
        } finally {
            disruptor.getRingBuffer().publish(sequence);
        }
    }

    public void start() {
        running.set(true);
        logger.info("Market data feed handler started");
    }

    public void stop() {
        running.set(false);
        disruptorsByInstrument.values().forEach(Disruptor::halt);
        feedExecutor.shutdown();
        try {
            if (!feedExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                feedExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            feedExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("Market data feed handler stopped");
    }

    public Map<String, Long> getLastUpdateTimestamps() {
        return Map.copyOf(lastUpdateTimestamps);
    }

    public boolean isRunning() {
        return running.get();
    }

    private class MarketDataEventHandler implements EventHandler<MarketDataEvent> {
        private final String instrument;

        MarketDataEventHandler(String instrument) {
            this.instrument = instrument;
        }

        @Override
        public void onEvent(MarketDataEvent event, long sequence, boolean endOfBatch) {
            try {
                switch (event.getEventType()) {
                    case ORDERBOOK -> handleOrderBookUpdate(event);
                    case TRADE -> handleTrade(event);
                }
                lastUpdateTimestamps.put(instrument, System.nanoTime());
            } catch (Exception e) {
                logger.error("Error processing market data event for {}: {}", instrument, e.getMessage(), e);
            }
        }

        private void handleOrderBookUpdate(MarketDataEvent event) {
            RiskEngineShard shard = instrumentShards.get(instrument);
            if (shard != null) {
                VaRModel varModel = shard.getVaRModel();
                varModel.updateOrderBook(
                    event.getBidPrices(),
                    event.getBidVolumes(),
                    event.getAskPrices(),
                    event.getAskVolumes(),
                    event.getTimestamp()
                );
            }
        }

        private void handleTrade(MarketDataEvent event) {
            RiskEngineShard shard = instrumentShards.get(instrument);
            if (shard != null) {
                VaRModel varModel = shard.getVaRModel();
                varModel.addTrade(
                    event.getTradePrice(),
                    event.getTradeVolume(),
                    event.getTimestamp()
                );
            }
        }
    }

    private static class DisruptorErrorHandler implements ExceptionHandler<MarketDataEvent> {
        private static final Logger errorLogger = LogManager.getLogger("DisruptorError");
        private final String instrument;

        DisruptorErrorHandler(String instrument) {
            this.instrument = instrument;
        }

        @Override
        public void handleEventException(Throwable ex, long sequence, MarketDataEvent event) {
            errorLogger.error("Disruptor exception on {} sequence {}: {}", 
                instrument, sequence, ex.getMessage(), ex);
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            errorLogger.error("Disruptor start exception for {}: {}", instrument, ex.getMessage(), ex);
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            errorLogger.error("Disruptor shutdown exception for {}: {}", instrument, ex.getMessage(), ex);
        }
    }

    public static class MarketDataEvent {
        private String instrument;
        private EventType eventType;
        private double[] bidPrices;
        private double[] bidVolumes;
        private double[] askPrices;
        private double[] askVolumes;
        private double tradePrice;
        private double tradeVolume;
        private Instant timestamp;
        private long sequenceNumber;

        public enum EventType {
            ORDERBOOK, TRADE
        }

        public String getInstrument() { return instrument; }
        public void setInstrument(String instrument) { this.instrument = instrument; }
        public EventType getEventType() { return eventType; }
        public void setEventType(EventType eventType) { this.eventType = eventType; }
        public double[] getBidPrices() { return bidPrices; }
        public void setBidPrices(double[] bidPrices) { this.bidPrices = bidPrices; }
        public double[] getBidVolumes() { return bidVolumes; }
        public void setBidVolumes(double[] bidVolumes) { this.bidVolumes = bidVolumes; }
        public double[] getAskPrices() { return askPrices; }
        public void setAskPrices(double[] askPrices) { this.askPrices = askPrices; }
        public double[] getAskVolumes() { return askVolumes; }
        public void setAskVolumes(double[] askVolumes) { this.askVolumes = askVolumes; }
        public double getTradePrice() { return tradePrice; }
        public void setTradePrice(double tradePrice) { this.tradePrice = tradePrice; }
        public double getTradeVolume() { return tradeVolume; }
        public void setTradeVolume(double tradeVolume) { this.tradeVolume = tradeVolume; }
        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
        public long getSequenceNumber() { return sequenceNumber; }
        public void setSequenceNumber(long sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    }
}