package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.QcSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQcHoldRequest(
        Long jobOrderId,
        @NotBlank String reason,
        @NotNull QcSeverity severity) {
}
