package com.shanwije.backpacker.security;

import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.request.SignInRequest;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Predicate;

public class Validation {

    private Validation() {
    }

    public static Predicate<String> isValidString = (s)-> ( s != null && !s.isEmpty());

    public static void validatePassword(PasswordEncoder passwordEncoder, SignInRequest signInRequest, UserDocument userDetails) {
        if (!passwordEncoder.matches(signInRequest.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
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

    public static UserDocument authenticateAndValidateUser(PasswordEncoder passwordEncoder,
                                                           SignInRequest signInRequest, UserDocument userDetails) {
        validatePassword(passwordEncoder, signInRequest, userDetails);
        validateAccount(userDetails);
        return userDetails;
    }
}
