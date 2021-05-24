package com.shanwije.backpacker.security.service;

import com.shanwije.backpacker.security.Validation;
import com.shanwije.backpacker.security.config.AuthenticationManager;
import com.shanwije.backpacker.security.config.JWTUtil;
import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.repository.RolesRepository;
import com.shanwije.backpacker.security.repository.UserRepository;
import com.shanwije.backpacker.security.request.TokenRequest;
import com.shanwije.backpacker.security.request.SignInRequest;
import com.shanwije.backpacker.security.request.SignUpRequest;
import com.shanwije.backpacker.security.response.TokenResponse;
import com.shanwije.backpacker.security.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;


@Service
@AllArgsConstructor
public class AuthService {

    RolesRepository rolesRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JWTUtil jwtUtil;

    public Mono<UserResponse> signUp(SignUpRequest signUpRequest) {

        return userRepository.findByUsername(signUpRequest.getUsername())
                .flatMap(existingUser -> Mono.error(new BadCredentialsException(signUpRequest.getUsername() + " : username already exist")))
                .switchIfEmpty(Mono.defer(() -> {
                    signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
                    return rolesRepository.findByAuthority("ROLE_USER").flatMap(defaultRole -> {
                        var userDocument = new UserDocument(signUpRequest, defaultRole);
                        return userRepository.save(userDocument).flatMap(userDocument1 ->
                                Mono.just(new UserResponse(userDocument1)));
                    });
                })).cast(UserResponse.class);
    }

    public Mono<TokenResponse> signIn(SignInRequest signInRequest) {
        return userRepository
                .findByUsername(signInRequest.getUsername())
                .map(userDetails -> authenticateAndValidateUser(signInRequest, userDetails))
                .map(jwtUtil::getJwtTokenResponse)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username or password")));
    }

    private UserDocument authenticateAndValidateUser(SignInRequest signInRequest, UserDocument userDetails) {
        Validation.validatePassword(passwordEncoder, signInRequest, userDetails);
        Validation.validateAccount(userDetails);
        return userDetails;
    }


    public Mono<TokenResponse> refresh(TokenRequest request) {
        String rToken = request.getRefreshToken();
        String username = request.getUsername();
        if(jwtUtil.isTokenValid(rToken, username)){
            return userRepository
                    .findByUsername(username)
                    .filter(Objects::nonNull)
                    .switchIfEmpty(Mono.error(new UsernameNotFoundException("Token associated User Account not found")))
                    .cast(UserDocument.class)
                    .map(userDetails -> jwtUtil.getJwtTokenResponse(userDetails, rToken))
                    .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username or password")));
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
}
