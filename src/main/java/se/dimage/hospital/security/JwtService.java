package se.dimage.hospital.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String jwtSecret = "aosdjoqjwdqjwndkqhdoijqowdjlqwkdlnklqwjdnlo1";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails
                .getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build()
                .parseSignedClaims(token).getPayload();
    }

    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
}
