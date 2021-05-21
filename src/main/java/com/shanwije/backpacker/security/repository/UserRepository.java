package com.shanwije.backpacker.security.repository;

import com.shanwije.backpacker.security.documents.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {

    Mono<UserDocument> findByUsername(final String username);

}
