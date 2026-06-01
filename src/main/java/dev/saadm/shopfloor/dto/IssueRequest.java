package dev.saadm.shopfloor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record IssueRequest(
        @NotBlank String sku,
        @NotNull @Positive BigDecimal quantity,
        String reference) {
}
