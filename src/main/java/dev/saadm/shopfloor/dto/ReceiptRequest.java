package dev.saadm.shopfloor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ReceiptRequest(
        @NotBlank String sku,
        @NotNull @Positive BigDecimal quantity,
        @NotNull @PositiveOrZero BigDecimal unitCost) {
}
