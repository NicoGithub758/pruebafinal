package uy.com.provider.hcen_document_provider_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date; 

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final long sessionExpirationMs; 

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.session.expiration-ms}") long sessionExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.sessionExpirationMs = sessionExpirationMs;
    }

    public String generateToken(String subject, String tenantId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + sessionExpirationMs);

        return Jwts.builder()
                .setSubject(subject)
                .claim("tenant_id", tenantId)
                .claim("rol", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Claims validateAndGetClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}