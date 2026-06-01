package dev.saadm.shopfloor.service;

import dev.saadm.shopfloor.domain.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/** Issues short-lived HS256 JWTs carrying the user's role as a claim. */
@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final long ttlMinutes;

    public JwtService(JwtEncoder encoder, @Value("${app.jwt.ttl-minutes}") long ttlMinutes) {
        this.encoder = encoder;
        this.ttlMinutes = ttlMinutes;
    }

    public TokenResult issue(AppUser user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(ttlMinutes, ChronoUnit.MINUTES);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("shopfloor-api")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getUsername())
                .claim("roles", List.of(user.getRole().name()))
                .build();
        String token = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenResult(token, expiresAt);
    }

    public record TokenResult(String token, Instant expiresAt) {
    }
}
