package dev.saadm.shopfloor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateLineRequest(
        @NotBlank String code,
        @NotBlank String name,
        @Positive int ratedUnitsPerHour) {
}
