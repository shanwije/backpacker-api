package com.shanwije.backpacker.security;

import com.shanwije.backpacker.security.config.CustomPasswordEncoder;
import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.repository.RolesRepository;
import com.shanwije.backpacker.security.repository.UserRepository;
import com.shanwije.backpacker.security.request.UserAuthenticationRequest;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.response.UserAuthenticationResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class AuthService {

    RolesRepository rolesRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public Mono<UserAuthenticationResponse> register(UserRegistrationRequest userRegistrationRequest) {
        userRegistrationRequest.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        UserDocument userDocument = new UserDocument(userRegistrationRequest);
        return Flux.fromIterable(userRegistrationRequest.getAuthorities())
                .flatMap(authority -> rolesRepository.findByAuthority(authority))
                .collectList().flatMap(roles -> {
                    userDocument.setAuthorities(roles);
                    return userRepository.save(userDocument).flatMap(userDocument1 -> Mono.just(new UserAuthenticationResponse(userDocument1)));
                });
    }
}
