package com.shanwije.backpacker.service;

import com.shanwije.backpacker.entities.User;
import com.shanwije.backpacker.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Flux<User> findAll(){
        return userRepository.findAll();
    }

    public Mono<User> findById(String id){
        return userRepository.findById(id);
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Mono<User> delete(String id) {
        return userRepository.findById(id).flatMap(user -> userRepository.deleteById(user.getId()).thenReturn(user));
    }
}
