package com.shanwije.backpacker.security;

import com.shanwije.backpacker.security.config.AuthenticationManager;
import com.shanwije.backpacker.security.config.JWTUtil;
import com.shanwije.backpacker.security.documents.RoleDocument;
import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.repository.RolesRepository;
import com.shanwije.backpacker.security.repository.UserRepository;
import com.shanwije.backpacker.security.request.UserAuthenticationRequest;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.response.TokenAuthenticationResponse;
import com.shanwije.backpacker.security.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class AuthService {

    RolesRepository rolesRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JWTUtil jwtUtil;

    public Mono<UserResponse> register(UserRegistrationRequest userRegistrationRequest) {

        return userRepository.findByUsername(userRegistrationRequest.getUsername())
                .flatMap(__ -> Mono.error(new BadCredentialsException(userRegistrationRequest.getUsername() + " : username already exist")))
                .switchIfEmpty(Mono.defer(() -> {
                    userRegistrationRequest.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
                    return rolesRepository.findByAuthority("ROLE_USER").flatMap(defaultRole -> {
                        UserDocument userDocument = new UserDocument(userRegistrationRequest, defaultRole);
                        return userRepository.save(userDocument).flatMap(userDocument1 ->
                                Mono.just(new UserResponse(userDocument1)));
                    });
                })).cast(UserResponse.class);
    }

    public Mono<TokenAuthenticationResponse> getToken(UserAuthenticationRequest userAuthenticationRequest) {
        return userRepository.findByUsername(userAuthenticationRequest.getUsername())
                .map((userDetails) ->
                        new TokenAuthenticationResponse(validateAndGetToken(userAuthenticationRequest, userDetails))
                );
    }

    private String validateAndGetToken(UserAuthenticationRequest userAuthenticationRequest, UserDocument userDetails) {
        if (passwordEncoder.matches(userAuthenticationRequest.getPassword(), userDetails.getPassword())) {
            if (!userDetails.isEnabled()) {
                throw new DisabledException(
                        "User is disabled");
            } else if (!userDetails.isAccountNonLocked()) {
                throw new LockedException(
                        "User account is locked");
            } else if (!userDetails.isAccountNonExpired()) {
                throw new AccountExpiredException(
                        "User account has expired");
            } else {
                return jwtUtil.generateToken(userDetails);
            }

        } else {
            throw new BadCredentialsException("invalid username or password");
        }
    }

}
