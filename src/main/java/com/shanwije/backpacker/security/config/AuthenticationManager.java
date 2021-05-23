package com.shanwije.backpacker.security.config;

import com.shanwije.backpacker.security.documents.RoleDocument;
import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.shanwije.backpacker.security.Validation.validateAccount;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {


    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        var token = authentication.getCredentials().toString();
        var username = jwtUtil.getUsernameFromToken(token);

        return userRepository.findByUsername(username)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Token associated User Account not found")))
                .cast(UserDocument.class)
                .map(userDetails -> {
                    if (validateAccount(userDetails)) {
                        List<SimpleGrantedAuthority> authorities = userDetails.getAuthorities()
                                .stream().map((RoleDocument role) -> new SimpleGrantedAuthority(role.getAuthority()))
                                .collect(Collectors.toList());
                        return new UsernamePasswordAuthenticationToken(
                                username, null, authorities);
                    }
                    return null;
                });
    }
}
