package com.shanwije.backpacker.config.security;

import com.shanwije.backpacker.repository.UserRepository;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JWTUtil jwtUtil;
    private UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtUtil.getUsernameFromToken(token);

        return userRepository.findByUsername(username)
                .flatMap(userDetails -> {
                    if (username.equals(userDetails.getUsername()) && jwtUtil.isTokenValid(token)) {
                        return Mono.just(authentication);
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
