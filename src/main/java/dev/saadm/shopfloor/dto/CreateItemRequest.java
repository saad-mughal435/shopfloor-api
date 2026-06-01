package dev.saadm.shopfloor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateItemRequest(
        @Schema(example = "FILM-SHRINK-50") @NotBlank String sku,
        @Schema(example = "Shrink film 50um") @NotBlank String name,
        @Schema(example = "ROLL") @NotBlank String uom) {
}
