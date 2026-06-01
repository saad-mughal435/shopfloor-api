package dev.saadm.shopfloor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateJobOrderRequest(
        @NotBlank String orderNo,
        @NotNull Long lineId,
        @NotBlank String product,
        @Positive int plannedQty,
        @Positive int plannedRuntimeMinutes) {
}
