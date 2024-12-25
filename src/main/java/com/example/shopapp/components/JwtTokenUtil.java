package com.example.shopapp.components;

import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private  long expirationTime;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws Exception {
        // properties => claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("user_id", user.getId());

        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            throw new InvalidParamException("Cannot generate token with error: " + e.getMessage());
        }
    }

    private Key getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
    // Decode
    private Claims extractToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractToken(token).getExpiration();
        return expiration.before(new Date());
    }

    public String extractPhoneNumber(String token) {
        return extractToken(token).getSubject();
    }


}
