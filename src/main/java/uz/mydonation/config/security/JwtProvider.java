package uz.mydonation.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${secret.key}")
    private String key;

    @Value("${jwt.hours}")
    private int hours;

    public String generate(Long id){
        return Jwts.builder()
                .issuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * hours))
                .subject(id.toString())
                .compact();
    }

    public Long extractId(String token){
        return Long.parseLong(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(key.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }
}
