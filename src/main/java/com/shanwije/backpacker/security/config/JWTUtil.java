package com.shanwije.backpacker.security.config;

import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.repository.UserRepository;
import com.shanwije.backpacker.security.response.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    public String TOKEN_TYPE = "TOKEN_TYPE";
    public String ID_TOKEN = "ID_TOKEN";
    public String USER_ID = "USER_ID";
    public static String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static String REFRESH_TOKEN = "REFRESH_TOKEN";

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jjwt.token.type}")
    private String tokenType;
    @Value("${jjwt.accesstoken.expiration}")
    private long accessTokenExpTimeInMills;
    @Value("${jjwt.refreshtoken.expiration}")
    private long refreshTokenExpTimeInMills;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public Mono<TokenResponse> getRefreshTokenResponse(UserDocument userDocument, String refreshToken) {
        return generateAccessToken(userDocument)
                .map(accessToken ->
                        new TokenResponse(accessToken,
                                refreshToken,
                                accessTokenExpTimeInMills,
                                refreshTokenExpTimeInMills,
                                tokenType));
    }

    public Mono<TokenResponse> getSignInResponse(UserDocument userDocument, UserRepository userRepository) {
        return generateAccessToken(userDocument)
                .flatMap(accessToken -> generateRefreshToken(userDocument, userRepository)
                        .map(refreshToken ->
                                new TokenResponse(
                                accessToken,
                                refreshToken,
                                accessTokenExpTimeInMills,
                                refreshTokenExpTimeInMills,
                                tokenType
                        )));
    }

    // TODO: 5/24/21 include device id in the validation
    public Mono<UserDocument> isRefreshTokenValid(String token, String id, UserRepository userRepository) {
        BCryptPasswordEncoder encorder = CustomPasswordEncoder.getPasswordEncoder();
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Token associated User Account not found")))
                .map(userDocument -> isRefreshTokenValid(id, encorder, userDocument, getClaimsFromToken(token)) ? userDocument : null);
    }

    private boolean isRefreshTokenValid(String id,
                                        BCryptPasswordEncoder encorder,
                                        UserDocument userDocument,
                                        Claims claims) {
        return claims.get(TOKEN_TYPE, String.class).equals(REFRESH_TOKEN) &&
                claims.get(USER_ID, String.class).equals(id) &&
                userDocument.getRefreshTokenIds().stream()
                        .anyMatch(tokenId ->
                                encorder.matches(tokenId, claims.get(ID_TOKEN, String.class)));
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public Mono<String> generateRefreshToken(UserDocument userDocument, UserRepository userRepository) {
        var encorder = CustomPasswordEncoder.getPasswordEncoder();
        String tokenId = UUID.randomUUID().toString();

        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_TOKEN, encorder.encode(tokenId));
        claims.put(TOKEN_TYPE, REFRESH_TOKEN);
        claims.put(USER_ID, userDocument.getId());

        return userRepository
                .save(userDocument.addRefreshTokenId(tokenId))
                .map(document -> getJwtToken(userDocument, refreshTokenExpTimeInMills, claims));
    }

    public Mono<String> generateAccessToken(UserDocument userDocument) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE, ACCESS_TOKEN);
        claims.put(USER_ID, userDocument.getId());
        return Mono.just(getJwtToken(userDocument, accessTokenExpTimeInMills, claims));
    }

    private String getJwtToken(UserDocument document, long expTime, Map<String, Object> claims) {
        var now = new Date();
        return Jwts.builder()
                .setSubject(document.getUsername())
                .setIssuedAt(now)
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + expTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

//    public Claims getClaimsFromToken(String token) {
//        return getClaims(token);
//    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        List<String> rolesList = getClaimsFromToken(token).get("auth", List.class);
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
