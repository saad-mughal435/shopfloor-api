package dev.saadm.shopfloor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record IssueRequest(
        @Schema(example = "PET-PREFORM-28G") @NotBlank String sku,
        @Schema(example = "1000") @NotNull @Positive BigDecimal quantity,
        @Schema(example = "JO-2026-003 consumption") String reference) {
}
