package com.trading.riskengine.application;

import com.trading.riskengine.domain.RiskOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.DoubleUnaryOperator;

public class KillSwitchPolicy {

    private static final Logger logger = LogManager.getLogger(KillSwitchPolicy.class);

    private static final double DEFAULT_ANOMALY_THRESHOLD = 3.0;
    private static final double DEFAULT_VOLATILITY_MULTIPLIER = 2.5;
    private static final int DEFAULT_WINDOW_SIZE = 100;
    private static final long DEFAULT_COOLDOWN_MS = 30_000L;
    private static final double MIN_THRESHOLD = 1.5;
    private static final double MAX_THRESHOLD = 5.0;

    private final Map<String, StrategyAnomalyTracker> strategyTrackers;
    private final AtomicBoolean globalKillSwitchActive;
    private final AtomicReference<Instant> lastTriggerTime;
    private final AtomicLong triggerCount;
    private final double baseAnomalyThreshold;
    private final double volatilityMultiplier;
    private final int windowSize;
    private final long cooldownMs;
    private final DoubleUnaryOperator adaptiveThresholdFunction;
    private volatile boolean enabled;

    public KillSwitchPolicy() {
        this(DEFAULT_ANOMALY_THRESHOLD, DEFAULT_VOLATILITY_MULTIPLIER, 
             DEFAULT_WINDOW_SIZE, DEFAULT_COOLDOWN_MS);
    }

    public KillSwitchPolicy(double anomalyThreshold, double volatilityMultiplier,
                           int windowSize, long cooldownMs) {
        this.strategyTrackers = new ConcurrentHashMap<>();
        this.globalKillSwitchActive = new AtomicBoolean(false);
        this.lastTriggerTime = new AtomicReference<>(Instant.EPOCH);
        this.triggerCount = new AtomicLong(0);
        this.baseAnomalyThreshold = anomalyThreshold;
        this.volatilityMultiplier = volatilityMultiplier;
        this.windowSize = windowSize;
        this.cooldownMs = cooldownMs;
        this.enabled = true;
        this.adaptiveThresholdFunction = this::calculateAdaptiveThreshold;
        logger.info("KillSwitchPolicy inicializada con threshold={}, multiplier={}, window={}",
                   anomalyThreshold, volatilityMultiplier, windowSize);
    }

    public boolean evaluateOrder(RiskOrder order) {
        if (!enabled) {
            return true;
        }

        if (globalKillSwitchActive.get()) {
            if (isWithinCooldown()) {
                logger.warn("Kill switch activo para orden {} - dentro del cooldown", order.orderId());
                return false;
            } else {
                globalKillSwitchActive.set(false);
                logger.info("Kill switch reseteado - cooldown expirado");
            }
        }

        String strategyId = order.strategyId();
        StrategyAnomalyTracker tracker = strategyTrackers.computeIfAbsent(
            strategyId, 
            k -> new StrategyAnomalyTracker(strategyId, windowSize)
        );

        AnomalyScore score = calculateAnomalyScore(order, tracker);
        double currentThreshold = adaptiveThresholdFunction.applyAsDouble(
            tracker.getRecentVolatility()
        );

        if (score.isAnomalous(currentThreshold)) {
            triggerKillSwitch(strategyId, order, score, currentThreshold);
            return false;
        }

        tracker.recordOrder(order, score);
        return true;
    }

    private AnomalyScore calculateAnomalyScore(RiskOrder order, StrategyAnomalyTracker tracker) {
        double orderSizeNormalizado = normalizeOrderSize(order.notional(), tracker);
        double deviationFromMean = calculateDeviationFromMean(orderSizeNormalizado, tracker);
        double zScore = deviationFromMean / Math.max(tracker.getStandardDeviation(), 0.001);
        
        double frequencyScore = calculateFrequencyAnomaly(order.traderId(), tracker);
        double sideImbalanceScore = calculateSideImbalance(tracker);
        
        double combinedScore = Math.abs(zScore) * 0.4 + frequencyScore * 0.3 + sideImbalanceScore * 0.3;
        
        return new AnomalyScore(combinedScore, zScore, frequencyScore, sideImbalanceScore);
    }

    private double normalizeOrderSize(BigDecimal notional, StrategyAnomalyTracker tracker) {
        double meanSize = tracker.getMeanOrderSize();
        if (meanSize <= 0) {
            return notional.doubleValue();
        }
        return notional.doubleValue() / meanSize;
    }

    private double calculateDeviationFromMean(double normalizedSize, StrategyAnomalyTracker tracker) {
        return normalizedSize - 1.0;
    }

    private double calculateFrequencyAnomaly(String traderId, StrategyAnomalyTracker tracker) {
        long ordersPerSecond = tracker.getOrdersPerSecond();
        long expectedOrdersPerSecond = tracker.getExpectedOrdersPerSecond();
        
        if (expectedOrdersPerSecond == 0) {
            return 0.0;
        }
        
        double ratio = (double) ordersPerSecond / expectedOrdersPerSecond;
        return Math.max(0.0, (ratio - 1.0) / 2.0);
    }

    private double calculateSideImbalance(StrategyAnomalyTracker tracker) {
        long buyCount = tracker.getBuyCount();
        long sellCount = tracker.getSellCount();
        long total = buyCount + sellCount;
        
        if (total == 0) {
            return 0.0;
        }
        
        return Math.abs((double)(buyCount - sellCount) / total);
    }

    private double calculateAdaptiveThreshold(double recentVolatility) {
        double adjustedThreshold = baseAnomalyThreshold * (1.0 + volatilityMultiplier * recentVolatility);
        return Math.max(MIN_THRESHOLD, Math.min(MAX_THRESHOLD, adjustedThreshold));
    }

    private void triggerKillSwitch(String strategyId, RiskOrder order, 
                                   AnomalyScore score, double threshold) {
        globalKillSwitchActive.set(true);
        lastTriggerTime.set(Instant.now());
        long count = triggerCount.incrementAndGet();
        
        logger.error("KILL SWITCH DISPARADO para estrategia {} - orden {} - " +
                    "score={} (threshold={}) - zScore={}, freqScore={}, imbalance={}",
                    strategyId, order.orderId(), score.combinedScore(), threshold,
                    score.zScore(), score.frequencyScore(), score.sideImbalanceScore());
        
        logger.error("DETALLE: traderId={}, notional={}, side={}, strategy={}",
                    order.traderId(), order.notional(), order.side(), order.strategyId());
        
        logger.warn("Trigger count total: {}", count);
    }

    private boolean isWithinCooldown() {
        Instant lastTrigger = lastTriggerTime.get();
        Duration elapsed = Duration.between(lastTrigger, Instant.now());
        return elapsed.toMillis() < cooldownMs;
    }

    public KillSwitchStatus getStatus() {
        return new KillSwitchStatus(
            enabled,
            globalKillSwitchActive.get(),
            triggerCount.get(),
            lastTriggerTime.get(),
            strategyTrackers.values().stream()
                .map(t -> new StrategyAnomalyStatus(
                    t.getStrategyId(),
                    t.getOrderCount(),
                    t.getMeanOrderSize(),
                    t.getStandardDeviation(),
                    t.getRecentVolatility()
                ))
                .toList()
        );
    }

    public void resetKillSwitch() {
        globalKillSwitchActive.set(false);
        logger.info("Kill switch reseteado manualmente");
    }

    public void enable() {
        this.enabled = true;
        logger.info("Kill switch habilitado");
    }

    public void disable() {
        this.enabled = false;
        logger.info("Kill switch deshabilitado");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isGlobalKillSwitchActive() {
        return globalKillSwitchActive.get();
    }

    public long getTriggerCount() {
        return triggerCount.get();
    }

    private static class StrategyAnomalyTracker {
        private final String strategyId;
        private final int windowSize;
        private final double[] recentOrderSizes;
        private final long[] recentOrderTimestamps;
        private int writeIndex;
        private long totalOrderCount;
        private long buyCount;
        private long sellCount;
        private double sumOrderSizes;
        private double sumSquaredSizes;
        private final AtomicLong orderCountLastSecond;
        private final long expectedOrdersPerSecond;
        private volatile double meanOrderSize;
        private volatile double standardDeviation;
        private volatile double recentVolatility;
        private volatile Instant lastOrderTime;

        StrategyAnomalyTracker(String strategyId, int windowSize) {
            this.strategyId = strategyId;
            this.windowSize = windowSize;
            this.recentOrderSizes = new double[windowSize];
            this.recentOrderTimestamps = new long[windowSize];
            this.writeIndex = 0;
            this.orderCountLastSecond = new AtomicLong(0);
            this.expectedOrdersPerSecond = 10;
            this.lastOrderTime = Instant.EPOCH;
        }

        void recordOrder(RiskOrder order, AnomalyScore score) {
            double notional = order.notional().doubleValue();
            long now = System.currentTimeMillis();

            recentOrderSizes[writeIndex] = notional;
            recentOrderTimestamps[writeIndex] = now;
            writeIndex = (writeIndex + 1) % windowSize;

            totalOrderCount++;
            sumOrderSizes += notional;
            sumSquaredSizes += notional * notional;

            if (order.side() == RiskOrder.OrderSide.BUY) {
                buyCount++;
            } else {
                sellCount++;
            }

            meanOrderSize = sumOrderSize() / Math.min(totalOrderCount, windowSize);
            standardDeviation = calculateStandardDeviation();
            recentVolatility = calculateRecentVolatility(now);
            lastOrderTime = Instant.ofEpochMilli(now);

            orderCountLastSecond.incrementAndGet();
        }

        private double sumOrderSize() {
            double sum = 0;
            for (int i = 0; i < windowSize; i++) {
                sum += recentOrderSizes[i];
            }
            return sum;
        }

        private double calculateStandardDeviation() {
            long count = Math.min(totalOrderCount, windowSize);
            if (count < 2) {
                return 0.0;
            }
            double variance = (sumSquaredSizes / count) - (meanOrderSize * meanOrderSize);
            return Math.sqrt(Math.max(0, variance));
        }

        private double calculateRecentVolatility(long now) {
            long oneSecondAgo = now - 1000;
            int count = 0;
            for (int i = 0; i < windowSize; i++) {
                if (recentOrderTimestamps[i] > oneSecondAgo) {
                    count++;
                }
            }
            return Math.min(1.0, count / (double) expectedOrdersPerSecond);
        }

        String getStrategyId() { return strategyId; }
        long getOrderCount() { return totalOrderCount; }
        double getMeanOrderSize() { return meanOrderSize; }
        double getStandardDeviation() { return standardDeviation; }
        double getRecentVolatility() { return recentVolatility; }
        long getBuyCount() { return buyCount; }
        long getSellCount() { return sellCount; }
        long getOrdersPerSecond() { return orderCountLastSecond.get(); }
        long getExpectedOrdersPerSecond() { return expectedOrdersPerSecond; }
    }

    private record AnomalyScore(
        double combinedScore,
        double zScore,
        double frequencyScore,
        double sideImbalanceScore
    ) {
        boolean isAnomalous(double threshold) {
            return combinedScore > threshold;
        }
    }

    private record StrategyAnomalyStatus(
        String strategyId,
        long orderCount,
        double meanOrderSize,
        double standardDeviation,
        double recentVolatility
    ) {}

    public record KillSwitchStatus(
        boolean enabled,
        boolean globalKillSwitchActive,
        long triggerCount,
        Instant lastTriggerTime,
        List<StrategyAnomalyStatus> strategyStatuses
    ) {}
}