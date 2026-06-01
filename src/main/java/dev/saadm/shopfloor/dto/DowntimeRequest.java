package dev.saadm.shopfloor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DowntimeRequest(
        @Schema(example = "12") @Positive int minutes,
        @Schema(example = "Filler short stop") @NotBlank String reason,
        @Schema(example = "Cap chute jam") String rootCause) {
}
