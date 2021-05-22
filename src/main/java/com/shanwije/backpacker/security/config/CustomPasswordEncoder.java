package com.shanwije.backpacker.security.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private CustomPasswordEncoder() {
    }

    public static BCryptPasswordEncoder getPasswordEncoder() {
        return bCryptPasswordEncoder;
    }
}
