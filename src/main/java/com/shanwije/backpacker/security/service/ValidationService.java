package com.shanwije.backpacker.security.service;

import com.shanwije.backpacker.security.documents.UserDocument;
import com.shanwije.backpacker.security.repository.UserRepository;
import com.shanwije.backpacker.security.request.ExisitingRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class ValidationService {

    UserRepository userRepository;

    public Mono<Boolean> isUnique(ExisitingRequest request) {
        String value = request.getValue();
        switch (request.getType()) {
            case "email": return userRepository
                    .findByEmail(value)
                    .mapNotNull(__ -> false)
                    .switchIfEmpty(Mono.just(true));


            case "username": return userRepository
                    .findByUsername(value)
                    .mapNotNull(__ -> false)
                    .switchIfEmpty(Mono.just(true));
            default:
                throw new IllegalStateException("Unexpected type: " + request.getType());
        }
    }
}
