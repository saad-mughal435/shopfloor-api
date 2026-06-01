package dev.saadm.shopfloor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateLineRequest(
        @Schema(example = "LINE-C") @NotBlank String code,
        @Schema(example = "Bottling Line C - 1L PET") @NotBlank String name,
        @Schema(example = "9000") @Positive int ratedUnitsPerHour) {
}
