package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.JobOrder;
import dev.saadm.shopfloor.domain.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record JobOrderResponse(
        Long id,
        String orderNo,
        String lineCode,
        String product,
        int plannedQty,
        int plannedRuntimeMinutes,
        JobStatus status,
        Integer goodUnits,
        Integer rejectUnits,
        Integer downtimeMinutes,
        BigDecimal availability,
        BigDecimal performance,
        BigDecimal quality,
        BigDecimal oee,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        LocalDateTime closedAt) {

    public static JobOrderResponse from(JobOrder j) {
        return new JobOrderResponse(
                j.getId(), j.getOrderNo(), j.getLine().getCode(), j.getProduct(),
                j.getPlannedQty(), j.getPlannedRuntimeMinutes(), j.getStatus(),
                j.getGoodUnits(), j.getRejectUnits(), j.getDowntimeMinutes(),
                j.getAvailability(), j.getPerformance(), j.getQuality(), j.getOee(),
                j.getCreatedAt(), j.getStartedAt(), j.getClosedAt());
    }
}
