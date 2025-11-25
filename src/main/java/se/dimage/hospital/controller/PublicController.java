package se.dimage.hospital.controller;

import lombok.RequiredArgsConstructor;
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
    private final ConfigProperties configProperties;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;

    @GetMapping
    public ResponseEntity<String> publicPage() {
        StringBuilder helloString = new StringBuilder("Hello!");
        configProperties.getEnvList().forEach(p -> helloString.append("\n<br/>").append(p));
        return ResponseEntity.ok(helloString.toString());
    }

    @GetMapping("/env")
    public  ResponseEntity<List<String>> envPage() {
        return ResponseEntity.ok(configProperties.getEnvKeys().stream().map(k -> k + ": " + configProperties.getProperty(k)).toList());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO login) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.username(), login.password())
        );
        UserDetails user = customUserDetailService.loadUserByUsername(login.username());

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
