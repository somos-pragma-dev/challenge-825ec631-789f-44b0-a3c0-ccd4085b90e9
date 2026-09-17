package com.trading.riskengine.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.atomic.StampedLock;
import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;

/**
 * Modelo de VaR (Value-at-Risk) intraday que calcula el riesgo en tiempo real.
 * Utiliza estructuras lock-free para actualización concurrente sin bloquear el procesamiento.
 * Implementa metodología de varianza-covarianza con ajustes por volatilidad histórica.
 */
public class VaRModel {
    private final String instrument;
    private final int lookbackPeriods;
    private final double confidenceLevel;
    private final double decayFactor;
    
    private final AtomicReference<double[]> returnsBuffer;
    private final AtomicReference<BigDecimal> currentVaR;
    private final AtomicReference<BigDecimal> currentVolatility;
    private final AtomicReference<Instant> lastUpdate;
    private final LongAdder calculationCount;
    private final StampedLock orderbookLock;
    
    private volatile double[] priceLevels;
    private volatile double[] volumes;
    private volatile int orderbookDepth;

    private static final int DEFAULT_LOOKBACK = 252;
    private static final double DEFAULT_CONFIDENCE = 0.99;
    private static final double DEFAULT_DECAY = 0.94;
    private static final double Z_SCORE_99 = 2.326;

    public VaRModel(String instrument) {
        this(instrument, DEFAULT_LOOKBACK, DEFAULT_CONFIDENCE, DEFAULT_DECAY);
    }

    public VaRModel(String instrument, int lookbackPeriods, double confidenceLevel, double decayFactor) {
        this.instrument = instrument;
        this.lookbackPeriods = lookbackPeriods;
        this.confidenceLevel = confidenceLevel;
        this.decayFactor = decayFactor;
        
        this.returnsBuffer = new AtomicReference<>(new double[lookbackPeriods]);
        this.currentVaR = new AtomicReference<>(BigDecimal.ZERO);
        this.currentVolatility = new AtomicReference<>(BigDecimal.ZERO);
        this.lastUpdate = new AtomicReference<>(Instant.now());
        this.calculationCount = new LongAdder();
        this.orderbookLock = new StampedLock();
        
        this.priceLevels = new double[50];
        this.volumes = new double[50];
        this.orderbookDepth = 0;
    }

    public void updateOrderBook(double[] bidPrices, double[] bidVolumes, 
                                 double[] askPrices, double[] askVolumes, int depth) {
        long stamp = orderbookLock.writeLock();
        try {
            int totalDepth = Math.min(depth, 50);
            this.orderbookDepth = totalDepth;
            
            for (int i = 0; i < totalDepth; i++) {
                priceLevels[i * 2] = bidPrices[i];
                volumes[i * 2] = bidVolumes[i];
                priceLevels[i * 2 + 1] = askPrices[i];
                volumes[i * 2 + 1] = askVolumes[i];
            }
            lastUpdate.set(Instant.now());
        } finally {
            orderbookLock.unlockWrite(stamp);
        }
    }

    public void addTrade(double price, double volume, Instant timestamp) {
        double[] currentReturns = returnsBuffer.get();
        double[] newReturns = new double[lookbackPeriods];
        
        System.arraycopy(currentReturns, 1, newReturns, 0, lookbackPeriods - 1);
        
        double lastPrice = priceLevels.length > 0 ? priceLevels[0] : price;
        double logReturn = Math.log(price / lastPrice);
        newReturns[lookbackPeriods - 1] = logReturn;
        
        returnsBuffer.set(newReturns);
        calculateVaR();
        calculationCount.increment();
    }

    private void calculateVaR() {
        double[] returns = returnsBuffer.get();
        double mean = DoubleStream.of(returns).filter(r -> r != 0).average().orElse(0.0);
        
        double variance = DoubleStream.of(returns)
            .filter(r -> r != 0)
            .map(r -> Math.pow(r - mean, 2))
            .average()
            .orElse(0.0);
        
        double volatility = Math.sqrt(variance);
        double annualizedVol = volatility * Math.sqrt(252);
        
        double currentPrice = getCurrentMidPrice();
        double varValue = currentPrice * annualizedVol * Z_SCORE_99 * Math.sqrt(1.0 / 252.0);
        
        currentVaR.set(BigDecimal.valueOf(varValue).setScale(2, RoundingMode.HALF_UP));
        currentVolatility.set(BigDecimal.valueOf(annualizedVol).setScale(4, RoundingMode.HALF_UP));
    }

    private double getCurrentMidPrice() {
        long stamp = orderbookLock.readLock();
        try {
            if (orderbookDepth > 0) {
                double bestBid = priceLevels[0];
                double bestAsk = priceLevels[1];
                return (bestBid + bestAsk) / 2.0;
            }
            return priceLevels.length > 0 ? priceLevels[0] : 100.0;
        } finally {
            orderbookLock.unlockRead(stamp);
        }
    }

    public BigDecimal getVaR() {
        return currentVaR.get();
    }

    public BigDecimal getVolatility() {
        return currentVolatility.get();
    }

    public boolean isBreached(BigDecimal positionValue) {
        BigDecimal var = currentVaR.get();
        return var.compareTo(BigDecimal.ZERO) > 0 && 
               positionValue.compareTo(var.multiply(BigDecimal.valueOf(1.5))) > 0;
    }

    public double getDynamicThreshold(double baseMultiplier) {
        BigDecimal vol = currentVolatility.get();
        double volMultiplier = vol.doubleValue() * Math.sqrt(1.0 / 252.0);
        return baseMultiplier * (1.0 + volMultiplier * 2.0);
    }

    public Instant getLastUpdateTime() {
        return lastUpdate.get();
    }

    public long getCalculationCount() {
        return calculationCount.sum();
    }

    public String getInstrument() {
        return instrument;
    }

    public VaRSnapshot getSnapshot() {
        return new VaRSnapshot(
            instrument,
            currentVaR.get(),
            currentVolatility.get(),
            getCurrentMidPrice(),
            lastUpdate.get(),
            calculationCount.sum()
        );
    }

    public record VaRSnapshot(
        String instrument,
        BigDecimal var,
        BigDecimal volatility,
        double midPrice,
        Instant lastUpdate,
        long calculationCount
    ) {}
}