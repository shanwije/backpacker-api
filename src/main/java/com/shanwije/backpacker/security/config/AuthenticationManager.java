package com.shanwije.backpacker.security.config;

import com.shanwije.backpacker.security.documents.RoleDocument;
import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
        if(!jwtUtil.isAccessToken(token)){
            throw new BadCredentialsException("Not an access token");
        }
        var username = jwtUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Token associated User Account not found")))
                .cast(UserDocument.class)
                .map(document -> {
                    if (validateAccount(document)) {
                        List<SimpleGrantedAuthority> authorities = document.getAuthorities()
                                .stream().map((RoleDocument role) -> new SimpleGrantedAuthority(role.getAuthority()))
                                .collect(Collectors.toList());
                        authorities.add(new SimpleGrantedAuthority(document.getId()));
                        return new UsernamePasswordAuthenticationToken(
                                document.getId(), null, authorities);
                    }
                    return null;
                });
    }
}
