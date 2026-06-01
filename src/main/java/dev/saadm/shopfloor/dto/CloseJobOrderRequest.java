package dev.saadm.shopfloor.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record CloseJobOrderRequest(
        @PositiveOrZero int goodUnits,
        @PositiveOrZero int rejectUnits) {
}
