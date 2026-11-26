package com.atinder.service_status_backend.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** Swaps the single admin's username and password for a short-lived JWT. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    static final Duration TOKEN_TTL = Duration.ofHours(1);

    private final JwtEncoder jwtEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public AuthController(JwtEncoder jwtEncoder,
                          @Value("${security.admin.username:admin}") String adminUsername,
                          @Value("${security.admin.password:}") String adminPassword) {
        this.jwtEncoder = jwtEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public record LoginRequest(String username, String password) {}

    @PostMapping("/token")
    public Map<String, Object> token(@RequestBody LoginRequest login) {
        if (adminPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Admin login is not configured");
        }
        // Constant-time compares, so response time doesn't leak how much of the password matched.
        boolean ok = equal(adminUsername, login.username()) & equal(adminPassword, login.password());
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("service-status")
            .subject(adminUsername)
            .issuedAt(now)
            .expiresAt(now.plus(TOKEN_TTL))
            .claim("scope", "admin")
            .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
            JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return Map.of("access_token", token, "token_type", "Bearer", "expires_in", TOKEN_TTL.toSeconds());
    }

    private static boolean equal(String expected, String given) {
        return given != null && MessageDigest.isEqual(
            expected.getBytes(StandardCharsets.UTF_8), given.getBytes(StandardCharsets.UTF_8));
    }
}
