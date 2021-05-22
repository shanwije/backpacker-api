package com.shanwije.backpacker.controller;

import com.shanwije.backpacker.config.MultiDataResponse;
import com.shanwije.backpacker.config.SingleDataResponse;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.response.UserResponse;
import com.shanwije.backpacker.security.service.UsersDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {


    UsersDetailsService usersDetailsService;
    MultiDataResponse multiDataResponse;
    SingleDataResponse singleDataResponse;


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<MultiDataResponse>> findAll() {
        return usersDetailsService.findAll()
                .collectList()
                .map(userResponses -> ResponseEntity.ok(multiDataResponse.setData(userResponses)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return usersDetailsService.findById(id)
                .map(body -> ResponseEntity.ok(body))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return usersDetailsService.delete(id).map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id, @RequestBody UserRegistrationRequest userRequest) {
        userRequest.setPassword(null);
        return usersDetailsService.findById(id)
                .flatMap(user1 -> usersDetailsService.update(user1.getId(), userRequest))
                .map(updatedUser -> new ResponseEntity<>(updatedUser, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
