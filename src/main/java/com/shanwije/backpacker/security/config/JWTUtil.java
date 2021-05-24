package com.shanwije.backpacker.security.config;

import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.response.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    public static String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static String REFRESH_TOKEN = "REFRESH_TOKEN";

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jjwt.token.type}")
    private String tokenType;
    @Value("${jjwt.accesstoken.expiration}")
    private String accessTokenExpTimeInMills;
    @Value("${jjwt.refreshtoken.expiration}")
    private String refreshTokenExpTimeInMills;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    // TODO: 5/24/21 keeping refresh and access token response generation separate for now. merge if it looks common in the future
    public TokenResponse getJwtTokenResponse(UserDetails userDetails, String refreshToken){
        return new TokenResponse(
                generateToken(userDetails, ACCESS_TOKEN),
                refreshToken,
                Long.parseLong(accessTokenExpTimeInMills),
                Long.parseLong(refreshTokenExpTimeInMills),
                tokenType
        );
    }

    public TokenResponse getJwtTokenResponse(UserDetails userDetails){
        return new TokenResponse(
                generateToken(userDetails, ACCESS_TOKEN),
                generateToken(userDetails, REFRESH_TOKEN),
                Long.parseLong(accessTokenExpTimeInMills),
                Long.parseLong(refreshTokenExpTimeInMills),
                tokenType
        );
    }

    // TODO: 5/24/21 include device id in the validation
    public Boolean isTokenValid(String token, String username) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject()
                .equals(username);
    }


    public String generateToken(UserDetails userDetails, String TOKEN_TYPE) {

        long expTime = (Long.parseLong(TOKEN_TYPE.equals(REFRESH_TOKEN)
                ? refreshTokenExpTimeInMills
                : accessTokenExpTimeInMills));

        var now = new Date();
        final var authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(TOKEN_TYPE)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        List<String> rolesList =  getClaimsFromToken(token).get("auth", List.class);
        return rolesList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDate(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    // token expiration automatically handled by spring security
    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }
}
