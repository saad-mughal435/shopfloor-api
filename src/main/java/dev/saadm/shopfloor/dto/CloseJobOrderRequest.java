package dev.saadm.shopfloor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

public record CloseJobOrderRequest(
        @Schema(example = "47000") @PositiveOrZero int goodUnits,
        @Schema(example = "800") @PositiveOrZero int rejectUnits) {
}
