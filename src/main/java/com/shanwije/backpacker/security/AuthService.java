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
import com.shanwije.backpacker.security.response.UserAuthenticationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;


@Service
@AllArgsConstructor
public class AuthService {

    RolesRepository rolesRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JWTUtil jwtUtil;

    public Mono<UserAuthenticationResponse> register(UserRegistrationRequest userRegistrationRequest) {
        userRegistrationRequest.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        userRegistrationRequest.setAuthorities(Arrays.asList("ROLE_USER"));
        UserDocument userDocument = new UserDocument(userRegistrationRequest);
        return Flux.fromIterable(userRegistrationRequest.getAuthorities())
                .flatMap(authority -> rolesRepository.findByAuthority(authority))
                .collectList().flatMap(roles -> {
                    userDocument.setAuthorities(roles);
                    return userRepository.save(userDocument).flatMap(userDocument1 ->
                            Mono.just(new UserAuthenticationResponse(userDocument1)));
                });
    }

    public Mono<TokenAuthenticationResponse> getToken(UserAuthenticationRequest userAuthenticationRequest) {
        return userRepository.findByUsername(userAuthenticationRequest.getUsername()).map((userDetails) -> {
            if (passwordEncoder.matches(userAuthenticationRequest.getPassword(), userDetails.getPassword())) {
                String token = jwtUtil.generateToken(userDetails);
                return new TokenAuthenticationResponse(token);
            } else {
                return null;
            }
        });
    }

}
