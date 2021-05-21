package com.shanwije.backpacker.security.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> authorities;
}
