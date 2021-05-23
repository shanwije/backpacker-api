package com.shanwije.backpacker.security.controller;

import com.shanwije.backpacker.config.core.ResponseWrapper;
import com.shanwije.backpacker.security.request.UserAuthenticationRequest;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.response.JwtTokenResponse;
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
@RequestMapping(value="/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    AuthService authService;
    ResponseWrapper<Object> responseWrapper;

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ResponseWrapper>> signUp(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return authService.signUp(userRegistrationRequest)
                .map(userResponse -> ResponseEntity.ok(responseWrapper.setData(userResponse)));
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<ResponseWrapper>> signIn(@Valid @RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        return authService.signIn(userAuthenticationRequest)
                .map((JwtTokenResponse tokenBody) -> ResponseEntity.ok(responseWrapper.setData(tokenBody)));
    }

}
