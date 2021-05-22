package com.shanwije.backpacker.security.controller;

import com.shanwije.backpacker.security.request.UserAuthenticationRequest;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.response.TokenAuthenticationResponse;
import com.shanwije.backpacker.security.response.UserResponse;
import com.shanwije.backpacker.security.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    AuthService authService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return authService.register(userRegistrationRequest);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<TokenAuthenticationResponse>> getToken(@Valid @RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        return authService.getToken(userAuthenticationRequest)
                .map(users -> ResponseEntity.ok(users));
    }

}
