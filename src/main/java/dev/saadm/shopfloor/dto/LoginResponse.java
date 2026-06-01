package dev.saadm.shopfloor.dto;

import dev.saadm.shopfloor.domain.Role;

import java.time.Instant;

public record LoginResponse(String token, Role role, Instant expiresAt) {
}
