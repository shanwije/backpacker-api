package com.shanwije.backpacker.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TokenResponse {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private long accessTokenexpiresIn;
    private long refreshTokenExpiresIn;

    public TokenResponse(String accessToken, String refreshToken, long accessTokenexpiresIn, long refreshTokenExpiresIn, String tokenType) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.accessTokenexpiresIn = accessTokenexpiresIn;
    }
}
