package dev.saadm.shopfloor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DowntimeRequest(
        @Positive int minutes,
        @NotBlank String reason,
        String rootCause) {
}
