package com.wdenberg.docegestao.security.jwt;

import com.wdenberg.docegestao.security.config.SecurityProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;


@Service
public class JwtService {

    private final SecurityProperties securityProperties;

    public JwtService(SecurityProperties propertiesSecurity){
        this.securityProperties = propertiesSecurity;
    }

    public String generateToken(String subject, Map<String, Object> claims){
        Instant now = Instant.now();
        Instant expiration = now.plus(securityProperties.accesTokenExpirationMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSignKey())
                .compact();
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject().equals(expectedUsername)
                && claims.getExpiration().after(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(securityProperties.secret().getBytes(StandardCharsets.UTF_8));
    }
}
