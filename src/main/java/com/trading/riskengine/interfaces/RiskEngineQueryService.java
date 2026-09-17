package com.trading.riskengine.interfaces;

import com.trading.riskengine.domain.RiskOrder;
import com.trading.riskengine.domain.VaRModel;
import com.trading.riskengine.domain.VaRModel.VaRSnapshot;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RiskEngineQueryService {

    Optional<RiskOrder> getOrderRiskEvaluation(String orderId);

    List<RiskOrder> getRiskDecisionsInRange(Instant startTime, Instant endTime);

    CircuitBreakerStatus getCircuitBreakerStatus();

    VaRSnapshot getVaRSnapshot(String instrument);

    BigDecimal getTraderExposure(String traderId);

    List<ExposureByStrategy> getStrategyExposures(String traderId);

    KillSwitchStatus getKillSwitchStatus();

    String generateComplianceReport(Instant startTime, Instant endTime);

    boolean isInstrumentEnabled(String instrument);

    record CircuitBreakerStatus(
        String instrument,
        CircuitState state,
        int breachCount,
        Instant lastBreachTime,
        Instant resetTime,
        String reason
    ) {
        public enum CircuitState {
            CLOSED, OPEN, HALF_OPEN, FORCED_OPEN
        }
    }

    record ExposureByStrategy(
        String strategyId,
        BigDecimal totalExposure,
        BigDecimal limit,
        double utilizationPercentage,
        int orderCount
    ) {}

    record KillSwitchStatus(
        boolean globalKillSwitchActive,
        String triggeredBy,
        Instant triggeredAt,
        String reason,
        List<String> affectedInstruments,
        boolean autoResetEnabled,
        Instant autoResetTime
    ) {}

    record ComplianceReport(
        Instant periodStart,
        Instant periodEnd,
        int totalOrders,
        int approvedOrders,
        int rejectedOrders,
        List<RiskOrder> rejectedAboveThreshold,
        BigDecimal totalNotionalApproved,
        BigDecimal totalNotionalRejected,
        int circuitBreakerTrips,
        int killSwitchActivations
    ) {}
}