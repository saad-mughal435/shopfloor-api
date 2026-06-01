package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.QcSeverity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQcHoldRequest(
        @Schema(example = "2") Long jobOrderId,
        @Schema(example = "Fill level below target on 3 of 20 sampled units") @NotBlank String reason,
        @Schema(example = "MEDIUM") @NotNull QcSeverity severity) {
}
