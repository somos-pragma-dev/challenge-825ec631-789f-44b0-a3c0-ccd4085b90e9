package com.trading.riskengine.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RiskOrder(
    String orderId,
    String traderId,
    String strategyId,
    String instrument,
    OrderSide side,
    BigDecimal quantity,
    BigDecimal price,
    BigDecimal notionalValue,
    BigDecimal currentVaR,
    BigDecimal exposure,
    BigDecimal traderLimit,
    BigDecimal strategyLimit,
    BigDecimal instrumentLimit,
    RiskStatus riskStatus,
    Instant timestamp,
    Instant evaluatedAt,
    String evaluationReason
) {
    public RiskOrder {
        if (orderId == null || orderId.isBlank()) {
            orderId = UUID.randomUUID().toString();
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (evaluatedAt == null) {
            evaluatedAt = timestamp;
        }
        if (riskStatus == null) {
            riskStatus = RiskStatus.PENDING;
        }
    }

    public enum OrderSide {
        BUY, SELL, SHORT, COVER
    }

    public enum RiskStatus {
        PENDING,
        APPROVED,
        REJECTED_LIMIT_BREACH,
        REJECTED_VAR_BREACH,
        REJECTED_CIRCUIT_BREAKER,
        REJECTED_KILL_SWITCH,
        REVIEW_REQUIRED
    }

    public boolean isApproved() {
        return riskStatus == RiskStatus.APPROVED;
    }

    public boolean isRejected() {
        return riskStatus.name().startsWith("REJECTED");
    }

    public boolean exceedsTraderLimit() {
        return traderLimit != null && exposure.compareTo(traderLimit) > 0;
    }

    public boolean exceedsStrategyLimit() {
        return strategyLimit != null && exposure.compareTo(strategyLimit) > 0;
    }

    public boolean exceedsInstrumentLimit() {
        return instrumentLimit != null && exposure.compareTo(instrumentLimit) > 0;
    }

    public boolean exceedsVaR() {
        return currentVaR != null && notionalValue.compareTo(currentVaR) > 0;
    }

    public RiskOrder withStatus(RiskStatus newStatus, String reason) {
        return new RiskOrder(
            orderId, traderId, strategyId, instrument, side, quantity, price,
            notionalValue, currentVaR, exposure, traderLimit, strategyLimit,
            instrumentLimit, newStatus, timestamp, Instant.now(), reason
        );
    }

    public BigDecimal getEffectiveLimit() {
        BigDecimal minLimit = traderLimit;
        if (strategyLimit != null && (minLimit == null || strategyLimit.compareTo(minLimit) < 0)) {
            minLimit = strategyLimit;
        }
        if (instrumentLimit != null && (minLimit == null || instrumentLimit.compareTo(minLimit) < 0)) {
            minLimit = instrumentLimit;
        }
        return minLimit;
    }

    public String toComplianceLog() {
        return String.format(
            "ORDER_DECISION: orderId=%s traderId=%s strategyId=%s instrument=%s side=%s " +
            "notional=%s status=%s reason=\"%s\" evaluatedAt=%s",
            orderId, traderId, strategyId, instrument, side, notionalValue,
            riskStatus, evaluationReason, evaluatedAt
        );
    }
}