package com.trading.riskengine.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.micrometer.CircuitBreakerMetrics;
import io.github.resilience4j.micrometer.RetryMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResilienceConfig {
    private static final Logger logger = LogManager.getLogger(ResilienceConfig.class);
    
    private static final int CB_WINDOW_SECONDS = 60;
    private static final int CB_MIN_CALLS = 10;
    private static final double CB_FAILURE_RATE_LOW_VOL = 40.0;
    private static final double CB_FAILURE_RATE_MED_VOL = 50.0;
    private static final double CB_FAILURE_RATE_HIGH_VOL = 65.0;
    private static final double CB_SLOW_CALL_RATE = 80.0;
    private static final Duration CB_SLOW_CALL_DURATION = Duration.ofSeconds(2);
    private static final Duration CB_WAIT_DURATION = Duration.ofSeconds(30);
    private static final int CB_HALF_OPEN_CALLS = 3;
    
    private static final int RETRY_MAX_ATTEMPTS = 3;
    private static final Duration RETRY_WAIT_DURATION = Duration.ofMillis(100);
    private static final double RETRY_MULTIPLIER = 2.0;
    private static final Duration RETRY_MAX_DURATION = Duration.ofSeconds(5);
    
    private static final int BULKHEAD_MAX_CONCURRENT = 100;
    private static final int BULKHEAD_MAX_WAIT_DURATION_MS = 500;
    
    private static final double VOLATILITY_LOW_THRESHOLD = 0.15;
    private static final double VOLATILITY_MED_THRESHOLD = 0.35;

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final Map<String, Double> volatilityCache;
    private final CircuitBreakerMetrics circuitBreakerMetrics;
    private final RetryMetrics retryMetrics;

    public ResilienceConfig() {
        this.circuitBreakerRegistry = CircuitBreakerRegistry.of(createDefaultConfig());
        this.retryRegistry = RetryRegistry.of(createDefaultRetryConfig());
        this.volatilityCache = new ConcurrentHashMap<>();
        this.circuitBreakerMetrics = new CircuitBreakerMetrics(circuitBreakerRegistry);
        this.retryMetrics = new RetryMetrics(retryRegistry);
        
        logger.info("ResilienceConfig initialized with circuit breakers and retries");
    }

    private CircuitBreakerConfig createDefaultConfig() {
        return CircuitBreakerConfig.custom()
            .slidingWindowSize(CB_WINDOW_SECONDS)
            .minimumNumberOfCalls(CB_MIN_CALLS)
            .failureRateThreshold(CB_FAILURE_RATE_MED_VOL)
            .slowCallRateThreshold(CB_SLOW_CALL_RATE)
            .slowCallDurationThreshold(CB_SLOW_CALL_DURATION)
            .waitDurationInOpenState(CB_WAIT_DURATION)
            .permittedNumberOfCallsInHalfOpenState(CB_HALF_OPEN_CALLS)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .recordExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .build();
    }

    private RetryConfig createDefaultRetryConfig() {
        return RetryConfig.custom()
            .maxAttempts(RETRY_MAX_ATTEMPTS)
            .waitDuration(RETRY_WAIT_DURATION)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(
                RETRY_WAIT_DURATION.toMillis(), 
                RETRY_MULTIPLIER, 
                RETRY_MAX_DURATION.toMillis()))
            .retryExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .build();
    }

    public CircuitBreaker getOrCreateCircuitBreaker(String name, double currentVolatility) {
        CircuitBreakerConfig config = createVolatilityCalibratedConfig(currentVolatility);
        
        if (!circuitBreakerRegistry.getAllCircuitBreakers().anyMatch(cb -> cb.getName().equals(name))) {
            CircuitBreaker cb = circuitBreakerRegistry.circuitBreaker(name, config);
            logger.info("Created circuit breaker {} with volatility calibration: {}", 
                name, currentVolatility);
            return cb;
        }
        
        CircuitBreaker existing = circuitBreakerRegistry.circuitBreaker(name);
        existing.changeConfig(config);
        return existing;
    }

    private CircuitBreakerConfig createVolatilityCalibratedConfig(double volatility) {
        double failureRate = calibrateFailureRate(volatility);
        
        return CircuitBreakerConfig.custom()
            .slidingWindowSize(CB_WINDOW_SECONDS)
            .minimumNumberOfCalls(CB_MIN_CALLS)
            .failureRateThreshold(failureRate)
            .slowCallRateThreshold(CB_SLOW_CALL_RATE)
            .slowCallDurationThreshold(CB_SLOW_CALL_DURATION)
            .waitDurationInOpenState(CB_WAIT_DURATION)
            .permittedNumberOfCallsInHalfOpenState(CB_HALF_OPEN_CALLS)
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .recordExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .build();
    }

    private double calibrateFailureRate(double volatility) {
        if (volatility < VOLATILITY_LOW_THRESHOLD) {
            return CB_FAILURE_RATE_LOW_VOL;
        } else if (volatility < VOLATILITY_MED_THRESHOLD) {
            return CB_FAILURE_RATE_MED_VOL;
        } else {
            return CB_FAILURE_RATE_HIGH_VOL;
        }
    }

    public Retry getOrCreateRetry(String name) {
        if (!retryRegistry.getAllRetries().anyMatch(r -> r.getName().equals(name))) {
            Retry retry = retryRegistry.retry(name);
            logger.info("Created retry configuration: {}", name);
            return retry;
        }
        return retryRegistry.retry(name);
    }

    public void updateVolatilityForInstrument(String instrument, double volatility) {
        volatilityCache.put(instrument, volatility);
        
        String cbName = "shard-" + instrument;
        if (circuitBreakerRegistry.getAllCircuitBreakers().anyMatch(cb -> cb.getName().equals(cbName))) {
            getOrCreateCircuitBreaker(cbName, volatility);
            logger.info("Updated volatility calibration for {}: {}", instrument, volatility);
        }
    }

    public CircuitBreakerRegistry getCircuitBreakerRegistry() {
        return circuitBreakerRegistry;
    }

    public RetryRegistry getRetryRegistry() {
        return retryRegistry;
    }

    public Map<String, Double> getVolatilityCache() {
        return Map.copyOf(volatilityCache);
    }

    public String getCircuitBreakerHealthSummary() {
        StringBuilder sb = new StringBuilder("Circuit Breaker Health Summary:\n");
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            CircuitBreaker.Metrics metrics = cb.getMetrics();
            sb.append(String.format("  %s: state=%s, failureRate=%.2f%%, slowCallRate=%.2f%%\n",
                cb.getName(),
                cb.getState(),
                metrics.getFailureRate(),
                metrics.getSlowCallRate()));
        });
        return sb.toString();
    }

    public String getRetryHealthSummary() {
        StringBuilder sb = new StringBuilder("Retry Health Summary:\n");
        retryRegistry.getAllRetries().forEach(retry -> {
            Retry.Metrics metrics = retry.getMetrics();
            sb.append(String.format("  %s: attempts=%d, successes=%d, failures=%d\n",
                retry.getName(),
                metrics.getNumberOfTotalCalls(),
                metrics.getNumberOfSuccessfulCallsWithRetry(),
                metrics.getNumberOfFailedCallsWithRetry()));
        });
        return sb.toString();
    }

    public void resetAllCircuitBreakers() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(cb -> {
            cb.reset();
            logger.info("Reset circuit breaker: {}", cb.getName());
        });
    }

    public double getCalibratedThreshold(String instrument) {
        Double volatility = volatilityCache.get(instrument);
        if (volatility == null) {
            return CB_FAILURE_RATE_MED_VOL;
        }
        return calibrateFailureRate(volatility);
    }
}