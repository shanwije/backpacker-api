package com.shanwije.backpacker.security.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    private String username;
    private String password;
    @Email
    private String email;
}
