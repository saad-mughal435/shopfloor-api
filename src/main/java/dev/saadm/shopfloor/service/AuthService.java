package dev.saadm.shopfloor.service;

import dev.saadm.shopfloor.domain.AppUser;
import dev.saadm.shopfloor.dto.LoginRequest;
import dev.saadm.shopfloor.dto.LoginResponse;
import dev.saadm.shopfloor.repo.AppUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AppUserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest req) {
        AppUser user = users.findByUsername(req.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        JwtService.TokenResult token = jwtService.issue(user);
        return new LoginResponse(token.token(), user.getRole(), token.expiresAt());
    }
}
