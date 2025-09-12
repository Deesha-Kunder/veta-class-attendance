package com.shadee.Veeta.utils;

import com.shadee.Veeta.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${app.jwt.secreteKey}")
    private String secreteKey;

    @Value("${app.jwt.expirationMs}")
    private Long expirationMs;

    @Value("${app.jwt.refreshExpiration}")
    private Long refreshExpirationMs;

    private SecretKey key;
    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secreteKey));
    }

    public String generateJwtToken(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Date expireDate = new Date(System.currentTimeMillis() + expirationMs);

        Map<String, Object>claims = new HashMap<>();
        claims.put("username",userDetails.getUsername());
        claims.put("userId",userDetails.getId());
        claims.put("role",userDetails.getAuthorities().iterator().next().getAuthority());

        return Jwts.builder()
                .subject(userDetails.getEmail())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }
    public String generateRefreshToken(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Date expireDate = new Date(System.currentTimeMillis() + refreshExpirationMs);

        Map<String, Object>claims = new HashMap<>();
        claims.put("username",userDetails.getUsername());
        claims.put("userId",userDetails.getId());
        claims.put("role",userDetails.getAuthorities().iterator().next().getAuthority());

        return Jwts.builder()
                .subject(userDetails.getEmail())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean validateJwtToken(String authToken, CustomUserDetails userDetails) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken)
                    .getPayload();
            String email = claims.getSubject();
            Date expireDate = claims.getExpiration();
            return (email.equals(userDetails.getEmail()) && expireDate.after(new Date()));
        } catch (Exception e) {
            System.err.println(" Invalid JWT :"+e.getMessage());
            return false;
        }
    }
}
