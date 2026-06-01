package dev.saadm.shopfloor.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateItemRequest(
        @NotBlank String sku,
        @NotBlank String name,
        @NotBlank String uom) {
}
