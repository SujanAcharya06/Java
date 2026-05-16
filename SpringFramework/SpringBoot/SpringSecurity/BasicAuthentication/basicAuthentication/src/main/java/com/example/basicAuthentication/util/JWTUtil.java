package com.example.basicAuthentication.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil{

    private final long EXPIRATION_TIME = 1000*60*60; //1hour

    private final String SECRET = "my-super-secret-key-that-is-long-enough-123456789!@#";
    private final SecretKey key  = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username){
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact(); // compact method will convert all these into single String
    }

    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token){
        return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    public boolean validateToken(String username, UserDetails userDetails, String token) {
        // Todo - check if username is same as username in userDetails
        // Todo - check if token is not expired
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());

    }

}
