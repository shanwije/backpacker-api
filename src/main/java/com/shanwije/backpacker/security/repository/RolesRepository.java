package com.shanwije.backpacker.security.repository;

import com.shanwije.backpacker.security.documents.RoleDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RolesRepository extends ReactiveMongoRepository<RoleDocument, String> {

    Mono<RoleDocument> findByAuthority(final String authority);

}
