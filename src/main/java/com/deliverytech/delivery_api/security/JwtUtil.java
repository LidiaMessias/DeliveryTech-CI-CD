package com.deliverytech.delivery_api.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
        /*try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ex) {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }*/
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof Usuario) {
            Usuario usuario = (Usuario) userDetails;
            claims.put("userId", usuario.getId());
            claims.put("role", usuario.getRole().name());
            claims.put("nome", usuario.getNome());
            if (usuario.getRestauranteId() != null) {
                claims.put("restauranteId", usuario.getRestauranteId());
            }
        }

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));

        /*Claims claims = extractAllClaims(token);
        Object val = claims.get("userId");
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).longValue();
        try { return Long.valueOf(String.valueOf(val)); } catch (Exception e) { return null; }*/
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractNome(String token) {
        return extractClaim(token, claims -> claims.get("nome", String.class));
    }

    public Long extractRestauranteId(String token) {
        return extractClaim(token, claims -> claims.get("restauranteId", Long.class));
        /* 
        Claims claims = extractAllClaims(token);
        Object val = claims.get("restauranteId");
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).longValue();
        try { return Long.valueOf(String.valueOf(val)); } catch (Exception e) { return null; } */
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

        /*Jws<Claims> jws = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token);
        return jws.getBody();*/
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

        /*Date exp = extractExpiration(token);
        return exp == null ? true : exp.before(new Date());*/
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}