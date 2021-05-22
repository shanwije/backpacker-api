package com.shanwije.backpacker.controller;

import com.shanwije.backpacker.security.service.AuthService;
import com.shanwije.backpacker.security.request.UserAuthenticationRequest;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.response.TokenAuthenticationResponse;
import com.shanwije.backpacker.security.response.UserResponse;
import com.shanwije.backpacker.security.service.UsersDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    UsersDetailsService usersDetailsService;


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Flux<ResponseEntity<UserResponse>> findAll() {
        return usersDetailsService.findAll()
                .map(users -> ResponseEntity.ok(users));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return usersDetailsService.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return usersDetailsService.delete(id).map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id, @RequestBody UserRegistrationRequest userRequest) {
        userRequest.setPassword(null);
        return usersDetailsService.findById(id)
                .flatMap(user1 -> usersDetailsService.update(user1.getId(), userRequest))
                .map(updatedUser -> new ResponseEntity<>(updatedUser, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
