package donatehub.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String key;

    @Value("${jwt.access.hours}")
    private int accessHours;

    @Value("${jwt.refresh.hours}")
    private int refreshHours;

    public String generateAccessToken(Long id) {
        return Jwts.builder()
                .issuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * accessHours))
                .subject(id.toString())
                .compact();
    }

    public String generateRefreshToken(Long id) {
        return Jwts.builder()
                .issuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * refreshHours))
                .subject(id.toString())
                .compact();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(
                Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(key.getBytes()))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject()
        );
    }
}
