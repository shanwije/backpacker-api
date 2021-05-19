package com.shanwije.backpacker.repository;

import com.shanwije.backpacker.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
