package dev.saadm.shopfloor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ReceiptRequest(
        @Schema(example = "PET-PREFORM-28G") @NotBlank String sku,
        @Schema(example = "5000") @NotNull @Positive BigDecimal quantity,
        @Schema(example = "0.0180") @NotNull @PositiveOrZero BigDecimal unitCost) {
}
