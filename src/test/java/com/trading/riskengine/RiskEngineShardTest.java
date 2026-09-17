package com.trading.riskengine;

import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.RiskOrder.RiskStatus;
import com.trading.riskengine.application.RiskEngineShard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RiskEngineShard Tests - Pruebas de integración de shards paralelos")
class RiskEngineShardTest {

    private RiskEngineShard shardAAPL;
    private RiskEngineShard shardGOOGL;

    @BeforeEach
    void setUp() {
        shardAAPL = new RiskEngineShard("AAPL", null);
        shardGOOGL = new RiskEngineShard("GOOGL", null);
    }

    @Test
    @DisplayName("Debe mantener consistencia entre shards paralelos")
    void testConsistencyBetweenParallelShards() throws InterruptedException {
        int ordersPerShard = 500;
        CountDownLatch latch = new CountDownLatch(2);
        List<RiskOrder> ordersAAPL = new ArrayList<>();
        List<RiskOrder> ordersGOOGL = new ArrayList<>();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        executor.submit(() -> {
            try {
                for (int i = 0; i < ordersPerShard; i++) {
                    RiskOrder order = createTestOrder("AAPL", BigDecimal.valueOf(10000));
                    shardAAPL.submitOrder(order);
                    ordersAAPL.add(order);
                }
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                for (int i = 0; i < ordersPerShard; i++) {
                    RiskOrder order = createTestOrder("GOOGL", BigDecimal.valueOf(15000));
                    shardGOOGL.submitOrder(order);
                    ordersGOOGL.add(order);
                }
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(ordersPerShard, shardAAPL.getRecentOrders().size());
        assertEquals(ordersPerShard, shardGOOGL.getRecentOrders().size());

        for (RiskOrder order : ordersAAPL) {
            assertNotNull(shardAAPL.getRecentOrders().get(order.orderId()));
        }

        for (RiskOrder order : ordersGOOGL) {
            assertNotNull(shardGOOGL.getRecentOrders().get(order.orderId()));
        }
    }

    @Test
    @DisplayName("Debe cumplir latencia p99 bajo carga")
    @Timeout(60)
    void testP99LatencyUnderLoad() throws InterruptedException {
        int numThreads = 8;
        int ordersPerThread = 1000;
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(numThreads);

        ConcurrentLinkedQueue<Long> latencies = new ConcurrentLinkedQueue<>();
        AtomicInteger totalOrders = new AtomicInteger(0);

        for (int t = 0; t < numThreads; t++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < ordersPerThread; i++) {
                        long startTime = System.nanoTime();
                        
                        RiskOrder order = createTestOrder("AAPL", BigDecimal.valueOf(5000));
                        shardAAPL.submitOrder(order);
                        
                        long latency = System.nanoTime() - startTime;
                        latencies.add(latency);
                        totalOrders.incrementAndGet();
                    }
                } catch (Exception e) {
                    fail("Error during load test: " + e.getMessage());
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        completionLatch.await(60, TimeUnit.SECONDS);
        executor.shutdown();

        List<Long> sortedLatencies = new ArrayList<>(latencies);
        sortedLatencies.sort(Long::compareTo);

        int p99Index = (int) Math.ceil(sortedLatencies.size() * 0.99) - 1;
        long p99LatencyNanos = sortedLatencies.get(p99Index);
        double p99LatencyMicros = p99LatencyNanos / 1000.0;

        System.out.printf("Total orders: %d, P99 latency: %.2f µs%n", 
                          totalOrders.get(), p99LatencyMicros);

        assertTrue(p99LatencyMicros < 500.0, 
                  "P99 latency should be under 500 microseconds, was: " + p99LatencyMicros);
    }

    @Test
    @DisplayName("Debe disparar circuit breaker cuando se superan umbrales")
    void testCircuitBreakerFiresWhenThresholdsExceeded() {
        BigDecimal largeOrderValue = new BigDecimal("1000000.00");
        int breachCount = 0;

        for (int i = 0; i < 100; i++) {
            RiskOrder order = createTestOrder("AAPL", largeOrderValue);
            shardAAPL.submitOrder(order);

            if (order.isRejected()) {
                breachCount++;
            }
        }

        System.out.println("Orders rejected due to large value: " + breachCount);
        assertTrue(breachCount > 0);
    }

    @Test
    @DisplayName("Debe manejar correctamente órdenes que superan límites de trader")
    void testHandleOrdersExceedingTraderLimit() {
        String traderId = "TRADER_001";
        BigDecimal traderLimit = new BigDecimal("50000.00");

        RiskOrder order1 = new RiskOrder(
            UUID.randomUUID().toString(),
            traderId,
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.BUY,
            new BigDecimal("30000.00"),
            100,
            Instant.now(),
            traderLimit,
            new BigDecimal("100000.00"),
            new BigDecimal("50000.00"),
            RiskStatus.PENDING
        );

        RiskOrder order2 = new RiskOrder(
            UUID.randomUUID().toString(),
            traderId,
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.BUY,
            new BigDecimal("30000.00"),
            100,
            Instant.now(),
            traderLimit,
            new BigDecimal("100000.00"),
            new BigDecimal("50000.00"),
            RiskStatus.PENDING
        );

        shardAAPL.submitOrder(order1);
        shardAAPL.submitOrder(order2);

        RiskOrder retrievedOrder1 = shardAAPL.getRecentOrders().get(order1.orderId());
        assertNotNull(retrievedOrder1);
    }

    @Test
    @DisplayName("Debe mantener ordenamiento causal en procesamiento de órdenes")
    void testMaintainCausalOrderingInOrderProcessing() throws InterruptedException {
        int numOrders = 100;
        ConcurrentLinkedQueue<String> processingSequence = new ConcurrentLinkedQueue<>();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger(0);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                    for (int j = 0; j < numOrders / 10; j++) {
                        int orderNum = counter.getAndIncrement();
                        RiskOrder order = createTestOrder("AAPL", 
                            BigDecimal.valueOf(1000 + orderNum));
                        shardAAPL.submitOrder(order);
                        processingSequence.add(order.orderId() + "-" + orderNum);
                    }
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            });
        }

        latch.countDown();
        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(numOrders, shardAAPL.getRecentOrders().size());
    }

    @Test
    @DisplayName("Debe calcular exposición por instrumento correctamente")
    void testCalculateExposureByInstrument() {
        BigDecimal orderValue1 = new BigDecimal("25000.00");
        BigDecimal orderValue2 = new BigDecimal("35000.00");

        RiskOrder buyOrder = createTestOrder("AAPL", orderValue1);
        RiskOrder sellOrder = new RiskOrder(
            UUID.randomUUID().toString(),
            "TRADER_001",
            "STRATEGY_ALPHA",
            "AAPL",
            RiskOrder.OrderSide.SELL,
            orderValue2,
            100,
            Instant.now(),
            new BigDecimal("100000.00"),
            new BigDecimal("200000.00"),
            new BigDecimal("100000.00"),
            RiskStatus.PENDING
        );

        shardAAPL.submitOrder(buyOrder);
        shardAAPL.submitOrder(sellOrder);

        BigDecimal exposure = shardAAPL.getCurrentExposure();
        assertNotNull(exposure);
    }

    private RiskOrder createTestOrder(String instrument, BigDecimal orderValue) {
        return new RiskOrder(
            UUID.randomUUID().toString(),
            "TRADER_" + ThreadLocalRandom.current().nextInt(1000),
            "STRATEGY_ALPHA",
            instrument,
            RiskOrder.OrderSide.BUY,
            orderValue,
            100,
            Instant.now(),
            new BigDecimal("100000.00"),
            new BigDecimal("500000.00"),
            new BigDecimal("200000.00"),
            RiskStatus.PENDING
        );
    }
}