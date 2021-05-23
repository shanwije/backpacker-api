package com.shanwije.backpacker.security;

import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.request.UserAuthenticationRequest;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Validation {

    private Validation() {
    }

    public static void validatePassword(PasswordEncoder passwordEncoder, UserAuthenticationRequest userAuthenticationRequest, UserDocument userDetails) {
        if (!passwordEncoder.matches(userAuthenticationRequest.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public static boolean validateAccount(UserDocument userDetails) {
        if (!userDetails.isEnabled()) {
            throw new DisabledException(
                    "User is disabled");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException(
                    "User account is locked");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException(
                    "User account has expired");
        }
        return true;
    }
}
