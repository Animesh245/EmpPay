package com.animesh245.emppay.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class AuthService implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    private final AuthSignInKeyResolver authSignInKeyResolver;

    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(authSignInKeyResolver)
                .build().parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails)
    {
        Map<String , Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String createToken(Map<String, Object> claims, String subject)
    {
        return Jwts.builder()
                .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //10 hours from now
                .signWith(authSignInKeyResolver.getSecretKey(), SignatureAlgorithm.HS256).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && Boolean.FALSE.equals(isTokenExpired(token)));
    }
}
