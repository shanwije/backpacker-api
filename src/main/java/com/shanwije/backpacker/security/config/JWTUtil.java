package com.shanwije.backpacker.security.config;

import com.shanwije.backpacker.security.documents.UserDocument;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWTUtil {
    private final String secret = "2342423444223424324244324234324324234234324243242342424242424242424242442424244242424242434245455654654565756756756785463543524325433423423434";
    private final String expireTimeInMiliSec = "3000000";

    private static final String AUTHORITIES_KEY = "auth";

    public String generateToken(UserDetails userDetails) {

        final var authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Map<String, Object> claim = new HashMap<>();


        return Jwts.builder()
                .claim("alg", "HS256")
                .claim("typ", "JWT")
                .claim(AUTHORITIES_KEY, authorities)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Long.parseLong(expireTimeInMiliSec) * 1000))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .setHeaderParams(claim)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDate(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDate(token);
        return expirationDate.before(new Date());
    }

    public Boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }
}
