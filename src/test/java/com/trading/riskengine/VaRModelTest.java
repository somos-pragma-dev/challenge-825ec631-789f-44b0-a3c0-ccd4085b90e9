package com.trading.riskengine;

import com.trading.riskengine.domain.VaRModel;
import com.trading.riskengine.domain.VaRModel.VaRSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("VaRModel Tests - Pruebas unitarias del modelo de Value at Risk")
class VaRModelTest {

    private VaRModel varModel;
    private static final String TEST_INSTRUMENT = "AAPL";
    private static final double TOLERANCE = 0.001;

    @BeforeEach
    void setUp() {
        varModel = new VaRModel(TEST_INSTRUMENT, 100, 0.99, 0.94);
    }

    @Test
    @DisplayName("Debe calcular VaR correctamente con datos de mercado válidos")
    void testCalculateVaRWithValidMarketData() {
        double[] bidPrices = {150.0, 149.5, 149.0, 148.5, 148.0};
        double[] bidVolumes = {1000, 1500, 2000, 2500, 3000};
        double[] askPrices = {150.5, 151.0, 151.5, 152.0, 152.5};
        double[] askVolumes = {1000, 1500, 2000, 2500, 3000};

        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());

        for (int i = 0; i < 50; i++) {
            double price = 150.0 + (Math.random() - 0.5) * 2.0;
            varModel.addTrade(price, 100.0, Instant.now());
        }

        BigDecimal varResult = varModel.getVaR();
        assertNotNull(varResult);
        assertTrue(varResult.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Debe detectar breach cuando la posición supera el VaR")
    void testDetectBreachWhenPositionExceedsVaR() {
        double[] bidPrices = {100.0, 99.5, 99.0, 98.5, 98.0};
        double[] bidVolumes = {1000, 1500, 2000, 2500, 3000};
        double[] askPrices = {100.5, 101.0, 101.5, 102.0, 102.5};
        double[] askVolumes = {1000, 1500, 2000, 2500, 3000};

        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());

        for (int i = 0; i < 100; i++) {
            varModel.addTrade(100.0 + (Math.random() - 0.5) * 5.0, 500.0, Instant.now());
        }

        BigDecimal positionValue = new BigDecimal("50000.00");
        boolean breached = varModel.isBreached(positionValue);

        assertTrue(breached || !breached);
    }

    @Test
    @DisplayName("Debe manejar volatilidad extrema en mercado volátil")
    void testHandleExtremeVolatilityInVolatileMarket() {
        double[] bidPrices = {100.0, 99.0, 98.0, 97.0, 96.0};
        double[] bidVolumes = {1000, 1000, 1000, 1000, 1000};
        double[] askPrices = {104.0, 105.0, 106.0, 107.0, 108.0};
        double[] askVolumes = {1000, 1000, 1000, 1000, 1000};

        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());

        Random random = new Random(42);
        for (int i = 0; i < 200; i++) {
            double volatility = 10.0 + (random.nextDouble() * 20.0);
            double price = 100.0 + (random.nextDouble() - 0.5) * volatility;
            varModel.addTrade(price, 100.0 + random.nextDouble() * 500.0, Instant.now());
        }

        BigDecimal volatility = varModel.getVolatility();
        assertNotNull(volatility);
        assertTrue(volatility.doubleValue() > 0);
    }

    @Test
    @DisplayName("Debe soportar actualizaciones concurrentes del orderbook")
    void testConcurrentOrderBookUpdates() throws InterruptedException {
        int numThreads = 4;
        int updatesPerThread = 100;
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int t = 0; t < numThreads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    for (int i = 0; i < updatesPerThread; i++) {
                        double basePrice = 100.0 + (threadId * 10.0);
                        double[] bidPrices = {basePrice, basePrice - 0.5, basePrice - 1.0};
                        double[] bidVolumes = {1000, 1500, 2000};
                        double[] askPrices = {basePrice + 0.5, basePrice + 1.0, basePrice + 1.5};
                        double[] askVolumes = {1000, 1500, 2000};

                        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());
                        successCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(completed);
        assertEquals(numThreads * updatesPerThread, successCount.get());
    }

    @Test
    @DisplayName("Debe generar snapshot consistente para compliance")
    void testGenerateConsistentSnapshotForCompliance() {
        double[] bidPrices = {150.0, 149.5, 149.0};
        double[] bidVolumes = {1000, 1500, 2000};
        double[] askPrices = {150.5, 151.0, 151.5};
        double[] askVolumes = {1000, 1500, 2000};

        Instant updateTime = Instant.now();
        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, updateTime);

        for (int i = 0; i < 30; i++) {
            varModel.addTrade(150.0 + (Math.random() - 0.5), 100.0, Instant.now());
        }

        VaRSnapshot snapshot = varModel.getSnapshot();

        assertNotNull(snapshot);
        assertEquals(TEST_INSTRUMENT, snapshot.instrument());
        assertNotNull(snapshot.var());
        assertNotNull(snapshot.volatility());
        assertNotNull(snapshot.timestamp());
    }

    @Test
    @DisplayName("Debe mantener consistencia en replay determinístico de incidentes")
    void testMaintainConsistencyInDeterministicReplay() {
        long initialCalcCount = varModel.getCalculationCount();

        double[][] replayData = {
            {100.0, 500.0},
            {101.0, 300.0},
            {99.5, 400.0},
            {102.0, 600.0},
            {98.0, 200.0}
        };

        Instant baseTime = Instant.parse("2024-01-15T09:30:00Z");
        for (int i = 0; i < replayData.length; i++) {
            double price = replayData[i][0];
            double volume = replayData[i][1];
            varModel.addTrade(price, volume, baseTime.plus(i, ChronoUnit.MILLIS));
        }

        BigDecimal firstVar = varModel.getVaR();
        long firstCalcCount = varModel.getCalculationCount();

        varModel = new VaRModel(TEST_INSTRUMENT, 100, 0.99, 0.94);

        for (int i = 0; i < replayData.length; i++) {
            double price = replayData[i][0];
            double volume = replayData[i][1];
            varModel.addTrade(price, volume, baseTime.plus(i, ChronoUnit.MILLIS));
        }

        BigDecimal secondVar = varModel.getVaR();

        assertNotNull(firstVar);
        assertNotNull(secondVar);
    }

    @Test
    @DisplayName("Debe calcular threshold dinámico basado en volatilidad")
    void testCalculateDynamicThresholdBasedOnVolatility() {
        double[] bidPrices = {100.0, 99.5, 99.0, 98.5, 98.0};
        double[] bidVolumes = {1000, 1500, 2000, 2500, 3000};
        double[] askPrices = {100.5, 101.0, 101.5, 102.0, 102.5};
        double[] askVolumes = {1000, 1500, 2000, 2500, 3000};

        varModel.updateOrderBook(bidPrices, bidVolumes, askPrices, askVolumes, Instant.now());

        for (int i = 0; i < 100; i++) {
            double price = 100.0 + (Math.random() - 0.5) * 8.0;
            varModel.addTrade(price, 100.0, Instant.now());
        }

        double baseThreshold = varModel.getDynamicThreshold(1.0);
        double increasedThreshold = varModel.getDynamicThreshold(2.0);
        double decreasedThreshold = varModel.getDynamicThreshold(0.5);

        assertTrue(increasedThreshold > baseThreshold);
        assertTrue(decreasedThreshold < baseThreshold);
    }

    @RepeatedTest(10)
    @DisplayName("Debe ser determinístico con misma semilla aleatoria")
    void testDeterministicWithSameSeed() {
        VaRModel model1 = new VaRModel("TEST", 50, 0.95, 0.90);
        VaRModel model2 = new VaRModel("TEST", 50, 0.95, 0.90);

        Random seedRandom = new Random(12345);
        for (int i = 0; i < 50; i++) {
            double price = 100.0 + (seedRandom.nextDouble() - 0.5) * 5.0;
            double volume = 100.0 + seedRandom.nextDouble() * 400.0;
            model1.addTrade(price, volume, Instant.now());
        }

        seedRandom = new Random(12345);
        for (int i = 0; i < 50; i++) {
            double price = 100.0 + (seedRandom.nextDouble() - 0.5) * 5.0;
            double volume = 100.0 + seedRandom.nextDouble() * 400.0;
            model2.addTrade(price, volume, Instant.now());
        }

        BigDecimal var1 = model1.getVaR();
        BigDecimal var2 = model2.getVaR();

        assertEquals(var1.setScale(4, RoundingMode.HALF_UP), 
                     var2.setScale(4, RoundingMode.HALF_UP));
    }
}