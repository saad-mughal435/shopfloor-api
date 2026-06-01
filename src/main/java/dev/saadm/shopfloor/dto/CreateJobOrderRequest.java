package dev.saadm.shopfloor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateJobOrderRequest(
        @Schema(example = "JO-2026-099") @NotBlank String orderNo,
        @Schema(example = "1") @NotNull Long lineId,
        @Schema(example = "0.5L Still Water - 24x0.5L shrink") @NotBlank String product,
        @Schema(example = "50000") @Positive int plannedQty,
        @Schema(example = "480") @Positive int plannedRuntimeMinutes) {
}
