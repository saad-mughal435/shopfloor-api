package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.DowntimeEvent;

import java.time.LocalDateTime;

public record DowntimeResponse(
        Long id,
        Long jobOrderId,
        int minutes,
        String reason,
        String rootCause,
        LocalDateTime occurredAt) {

    public static DowntimeResponse from(DowntimeEvent d) {
        return new DowntimeResponse(
                d.getId(), d.getJobOrder().getId(), d.getMinutes(),
                d.getReason(), d.getRootCause(), d.getOccurredAt());
    }
}
