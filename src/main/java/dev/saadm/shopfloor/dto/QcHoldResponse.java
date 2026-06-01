package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.QcHold;
import dev.saadm.shopfloor.domain.QcHoldStatus;
import dev.saadm.shopfloor.domain.QcSeverity;

import java.time.LocalDateTime;

public record QcHoldResponse(
        Long id,
        Long jobOrderId,
        String reason,
        QcSeverity severity,
        QcHoldStatus status,
        String raisedBy,
        LocalDateTime raisedAt,
        LocalDateTime releasedAt) {

    public static QcHoldResponse from(QcHold h) {
        return new QcHoldResponse(
                h.getId(),
                h.getJobOrder() == null ? null : h.getJobOrder().getId(),
                h.getReason(), h.getSeverity(), h.getStatus(),
                h.getRaisedBy(), h.getRaisedAt(), h.getReleasedAt());
    }
}
