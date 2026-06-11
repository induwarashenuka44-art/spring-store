package com.induwara.store.services;

import com.induwara.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;

    public String generate(User user){
        final long tokenExpiration = 86400; //1day

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

    }

    public boolean validateToken(String token){

        try {
            var claims = getClaims(token);

            return claims.getExpiration().after(new Date());
        } catch (JwtException ex){
            return false;
        }
    }

    private Claims getClaims(String token) {
            return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailFromToken(String token){
         return getClaims(token).get("email", String.class);
    }

    public Long getUserIdFromToken(String token){
         return Long.valueOf(getClaims(token).getSubject());
    }

    public String getNameFromToken(String token){
         return getClaims(token).get("name", String.class);
    }
}
