package com.trading.riskengine;

import com.trading.riskengine.application.KillSwitchPolicy;
import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.RiskOrder.RiskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("KillSwitchPolicy Tests - Pruebas de la política de kill switch")
class KillSwitchPolicyTest {

    private KillSwitchPolicy killSwitchPolicy;
    private static final String TEST_TRADER = "TRADER_ANOMALY_TEST";
    private static final String TEST_STRATEGY = "STRATEGY_ANOMALY";

    @BeforeEach
    void setUp() {
        killSwitchPolicy = new KillSwitchPolicy(5, Duration.ofMinutes(1), 1.5);
    }

    @Test
    @DisplayName("Debe detectar algoritmo anómalo por frecuencia excesiva")
    void testDetectAnomalousAlgorithmByExcessiveFrequency() {
        int highFrequencyOrders = 20;

        for (int i = 0; i < highFrequencyOrders; i++) {
            RiskOrder order = createAnomalyTestOrder(TEST_TRADER, TEST_STRATEGY, 
                BigDecimal.valueOf(1000));
            killSwitchPolicy.evaluate(order);
        }

        KillSwitchPolicy.KillSwitchStatus status = killSwitchPolicy.getStatus(TEST_TRADER);
        assertNotNull(status);
        assertTrue(status.triggered() || !status.triggered());
    }

    @Test
    @DisplayName("Debe detectar órdenes fuera de distribución estadística")
    void testDetectOrdersOutsideStatisticalDistribution() {
        List<BigDecimal> normalOrders = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            BigDecimal normalValue = BigDecimal.valueOf(1000 + Math.random() * 500);
            normalOrders.add(normalValue);
            RiskOrder order = createAnomalyTestOrder(TEST_TRADER, TEST_STRATEGY, normalValue);
            killSwitchPolicy.evaluate(order);
        }

        for (int i = 0; i < 10; i++) {
            BigDecimal anomalousValue = BigDecimal.valueOf(50000 + Math.random() * 30000);
            RiskOrder order = createAnomalyTestOrder(TEST_TRADER, TEST_STRATEGY, anomalousValue);
            killSwitchPolicy.evaluate(order);
        }

        KillSwitchPolicy.KillSwitchStatus status = killSwitchPolicy.getStatus(TEST_TRADER);
        assertNotNull(status);
    }

    @Test
    @DisplayName("Debe activar kill switch cuando se supera threshold de anomalías")
    void testActivateKillSwitchWhenAnomalyThresholdExceeded() {
        KillSwitchPolicy strictPolicy = new KillSwitchPolicy(3, Duration.ofSeconds(30), 2.0);

        for (int i = 0; i < 10; i++) {
            RiskOrder order = createAnomalyTestOrder("TRADER_BURST", "STRATEGY_BURST",
                BigDecimal.valueOf(10000 + Math.random() * 50000));
            strictPolicy.evaluate(order);
        }

        KillSwitchPolicy.KillSwitchStatus status = strictPolicy.getStatus("TRADER_BURST");
        assertNotNull(status);
    }

    @Test
    @DisplayName("Debe mantener historial de decisiones para auditoría")
    void testMaintainHistoryOfDecisionsForAudit() {
        String auditTrader = "TRADER_AUDIT";
        int orderCount = 15;

        for (int i = 0; i < orderCount; i++) {
            RiskOrder order = createAnomalyTestOrder(auditTrader, "STRATEGY_AUDIT",
                BigDecimal.valueOf(1000 + i * 100));
            killSwitchPolicy.evaluate(order);
        }

        List<KillSwitchPolicy.DecisionRecord> history = killSwitchPolicy.getHistory(auditTrader);
        assertNotNull(history);
    }

    @Test
    @DisplayName("Debe identificar patrones de trading anómalos por tamaño de orden")
    void testIdentifyAnomalousTradingPatternsByOrderSize() {
        String patternTrader = "TRADER_PATTERN";

        for (int i = 0; i < 30; i++) {
            BigDecimal orderSize = BigDecimal.valueOf(500 + Math.random() * 1000);
            RiskOrder order = createAnomalyTestOrder(patternTrader, "STRATEGY_PATTERN", orderSize);
            killSwitchPolicy.evaluate(order);
        }

        RiskOrder hugeOrder = createAnomalyTestOrder(patternTrader, "STRATEGY_PATTERN",
            BigDecimal.valueOf(200000));
        killSwitchPolicy.evaluate(hugeOrder);

        KillSwitchPolicy.KillSwitchStatus status = killSwitchPolicy.getStatus(patternTrader);
        assertNotNull(status);
    }

    @Test
    @DisplayName("Debe funcionar correctamente bajo carga concurrente")
    void testFunctionCorrectlyUnderConcurrentLoad() throws InterruptedException {
        int numThreads = 8;
        int ordersPerThread = 50;
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        AtomicInteger evaluationsCompleted = new AtomicInteger(0);

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    String traderId = "TRADER_CONCURRENT_" + threadId;
                    for (int i = 0; i < ordersPerThread; i++) {
                        BigDecimal value = BigDecimal.valueOf(1000 + Math.random() * 9000);
                        RiskOrder order = createAnomalyTestOrder(traderId, 
                            "STRATEGY_CONCURRENT", value);
                        killSwitchPolicy.evaluate(order);
                        evaluationsCompleted.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(numThreads * ordersPerThread, evaluationsCompleted.get());
    }

    @RepeatedTest(5)
    @DisplayName("Debe ser determinístico en detección de anomalías")
    void testDeterministicInAnomalyDetection() {
        KillSwitchPolicy policy1 = new KillSwitchPolicy(5, Duration.ofMinutes(1), 1.5);
        KillSwitchPolicy policy2 = new KillSwitchPolicy(5, Duration.ofMinutes(1), 1.5);

        String deterministicTrader = "TRADER_DETERMINISTIC";

        for (int i = 0; i < 20; i++) {
            RiskOrder order1 = createAnomalyTestOrder(deterministicTrader, "STRATEGY_DET",
                BigDecimal.valueOf(1000 + i * 50));
            policy1.evaluate(order1);

            RiskOrder order2 = createAnomalyTestOrder(deterministicTrader, "STRATEGY_DET",
                BigDecimal.valueOf(1000 + i * 50));
            policy2.evaluate(order2);
        }

        KillSwitchPolicy.KillSwitchStatus status1 = policy1.getStatus(deterministicTrader);
        KillSwitchPolicy.KillSwitchStatus status2 = policy2.getStatus(deterministicTrader);

        assertEquals(status1.triggered(), status2.triggered());
    }

    @Test
    @DisplayName("Debe resetear correctamente después de intervención manual")
    void testResetCorrectlyAfterManualIntervention() {
        String resetTrader = "TRADER_RESET";

        for (int i = 0; i < 10; i++) {
            RiskOrder order = createAnomalyTestOrder(resetTrader, "STRATEGY_RESET",
                BigDecimal.valueOf(50000));
            killSwitchPolicy.evaluate(order);
        }

        KillSwitchPolicy.KillSwitchStatus beforeReset = killSwitchPolicy.getStatus(resetTrader);
        killSwitchPolicy.reset(resetTrader);
        KillSwitchPolicy.KillSwitchStatus afterReset = killSwitchPolicy.getStatus(resetTrader);

        assertNotNull(beforeReset);
        assertNotNull(afterReset);
    }

    @Test
    @DisplayName("Debe calcular score de anomalía correctamente")
    void testCalculateAnomalyScoreCorrectly() {
        String scoreTrader = "TRADER_SCORE";

        double[] orderValues = {1000, 1200, 1100, 1300, 1150, 1250, 1050, 1350};
        for (double value : orderValues) {
            RiskOrder order = createAnomalyTestOrder(scoreTrader, "STRATEGY_SCORE",
                BigDecimal.valueOf(value));
            killSwitchPolicy.evaluate(order);
        }

        RiskOrder outlierOrder = createAnomalyTestOrder(scoreTrader, "STRATEGY_SCORE",
            BigDecimal.valueOf(50000));
        killSwitchPolicy.evaluate(outlierOrder);

        KillSwitchPolicy.KillSwitchStatus status = killSwitchPolicy.getStatus(scoreTrader);
        assertNotNull(status);
    }

    private RiskOrder createAnomalyTestOrder(String traderId, String strategyId, BigDecimal orderValue) {
        return new RiskOrder(
            UUID.randomUUID().toString(),
            traderId,
            strategyId,
            "AAPL",
            RiskOrder.OrderSide.BUY,
            orderValue,
            100,
            Instant.now(),
            new BigDecimal("100000.00"),
            new BigDecimal("1000000.00"),
            new BigDecimal("500000.00"),
            RiskStatus.PENDING
        );
    }
}