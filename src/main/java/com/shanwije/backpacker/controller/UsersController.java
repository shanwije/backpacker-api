package com.shanwije.backpacker.controller;

import com.shanwije.backpacker.config.core.ResponseWrapper;
import com.shanwije.backpacker.security.authority.IsAdmin;
import com.shanwije.backpacker.security.authority.IsUser;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.service.UsersDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {


    UsersDetailsService usersDetailsService;
    ResponseWrapper<Object> responseWrapper;

    @IsAdmin
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<ResponseWrapper>> findAll() {
        return usersDetailsService.findAll()
                .collectList()
                .map(userResponses -> ResponseEntity.ok(responseWrapper.setData(userResponses)));
    }

    @IsUser
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<ResponseWrapper>> findById(@PathVariable String id) {
        return usersDetailsService.findById(id)
                .map(userResponses -> ResponseEntity.ok(responseWrapper.setData(userResponses)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @IsUser
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<ResponseWrapper>> delete(@PathVariable String id) {
        return usersDetailsService.delete(id)
                .map(userResponses -> ResponseEntity.ok(responseWrapper.setData(userResponses)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @IsUser
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<ResponseWrapper>> update(@PathVariable String id, @RequestBody UserRegistrationRequest userRequest) {
        userRequest.setPassword(null);
        return usersDetailsService.findById(id)
                .flatMap(user1 -> usersDetailsService.update(user1.getId(), userRequest))
                .map(updatedUser -> ResponseEntity.ok(responseWrapper.setData(updatedUser)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
