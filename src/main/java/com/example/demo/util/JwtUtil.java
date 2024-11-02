package com.example.demo.util;

import com.example.demo.entity.MembershipEntity;
import com.example.demo.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.DecodingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey;
    private final long jwtExpirationInMs;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expiration}") long jwtExpirationInMs) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); // Base64 encode the secret
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String generateToken(MembershipEntity membership) {
        return Jwts.builder()
                .setSubject(Long.toString(membership.getId()))
                .claim("email", membership.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token tidak valid atau kadaluwarsa");
        }
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValidate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token expired");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.replace("Bearer ", "").trim())
                    .getBody();
        } catch (DecodingException e) {
            throw new RuntimeException("Invalid token format", e);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired", e);
        }
    }
}
