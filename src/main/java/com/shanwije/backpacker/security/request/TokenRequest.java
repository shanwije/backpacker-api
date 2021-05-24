package com.shanwije.backpacker.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {
    @NotBlank(message = "Refresh token value can not be blank")
    private String refreshToken;
    @NotBlank(message = "Username can not be blank")
    private String username;
}
