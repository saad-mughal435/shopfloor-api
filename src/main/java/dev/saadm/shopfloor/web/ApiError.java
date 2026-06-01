package dev.saadm.shopfloor.web;

import java.time.Instant;
import java.util.List;

/** Consistent JSON error body returned for every handled failure. */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        List<String> details) {

    public static ApiError of(int status, String error, String message, List<String> details) {
        return new ApiError(Instant.now(), status, error, message, details);
    }
}
