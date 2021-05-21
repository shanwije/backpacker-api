package com.shanwije.backpacker.service;

import com.shanwije.backpacker.security.repository.UserRepository;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.response.UserRegistrationResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@AllArgsConstructor
@Service
public class CustomUsersDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with login : " + username)))
                .cast(UserDetails.class);
    }

    public Mono<UserRegistrationResponse> findById(String id) {
        return userRepository.findById(id).flatMap(userDocument ->
                Mono.just(new UserRegistrationResponse(userDocument)));
    }

    public Flux<UserRegistrationResponse> findAll() {
        return userRepository.findAll().flatMap(userDocument ->
                Mono.just(new UserRegistrationResponse(userDocument)));
    }

    public Mono<Void> delete(String id) {
        return userRepository.findById(id).flatMap(userDocument ->
                userRepository.delete(userDocument));
    }

    public Mono<UserRegistrationResponse> update(String id, UserRegistrationRequest userRequest) {
        return userRepository.findById(id).flatMap(userDocument -> {
            userDocument.setUserRegistrationRequest(userRequest);
            return userRepository.save(userDocument).flatMap(userDocument1 ->
                    Mono.just(new UserRegistrationResponse(userDocument1)));
        });
    }
}
