package se.dimage.hospital.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import se.dimage.hospital.config.ConfigProperties;
import se.dimage.hospital.dto.JwtResponseDTO;
import se.dimage.hospital.dto.LoginRequestDTO;
import se.dimage.hospital.security.JwtService;
import se.dimage.hospital.service.CustomUserDetailService;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private static final Logger log = LoggerFactory.getLogger(PublicController.class);
    private final ConfigProperties configProperties;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;

    @GetMapping
    public ResponseEntity<String> publicPage() {
        log.info("Hello");
        StringBuilder helloString = new StringBuilder("Hello!");
        configProperties.getEnvList().forEach(p -> helloString.append("\n<br/>").append(p));
        return ResponseEntity.ok(helloString.toString());
    }

    @GetMapping("/env")
    public  ResponseEntity<List<String>> envPage() {
        log.info("List environment variables");
        return ResponseEntity.ok(configProperties.getEnvKeys().stream()
                .map(k -> k + ": " + configProperties.getProperty(k))
                .toList());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO login) {
        log.info("Logging in: " + login);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.username(), login.password())
        );

        UserDetails user = customUserDetailService.loadUserByUsername(login.username());

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(login.username());

        customUserDetailService.updateRefreshToken(login.username(), refreshToken);

        return ResponseEntity.ok(new JwtResponseDTO(token, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
        Claims claims;

        log.info("Refreshing the jwt token.");

        try {
            claims = jwtService.extractClaims(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Invalid refresh token");
        }

        String username = claims.getSubject();
        if (customUserDetailService.isRefreshTokenGood(username, refreshToken)) {
            String newAccessToken = jwtService.generateToken(customUserDetailService.loadUserByUsername(username));
            return ResponseEntity.ok(new JwtResponseDTO(newAccessToken, refreshToken));
        }
        return ResponseEntity.status(403).body("Invalid refresh token");
    }
}
