package com.shanwije.backpacker.security.controller;

import com.shanwije.backpacker.config.core.ResponseWrapper;
import com.shanwije.backpacker.security.request.SignInRequest;
import com.shanwije.backpacker.security.request.SignUpRequest;
import com.shanwije.backpacker.security.request.TokenRequest;
import com.shanwije.backpacker.security.response.TokenResponse;
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
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    AuthService authService;
    ResponseWrapper<Object> responseWrapper;

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<ResponseWrapper>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest)
                .map(userResponse -> ResponseEntity.ok(responseWrapper.setData(userResponse)));
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<ResponseWrapper>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest)
                .map((TokenResponse tokenBody) -> ResponseEntity.ok(responseWrapper.setData(tokenBody)));
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<ResponseWrapper>> refreshIn(@Valid @RequestBody TokenRequest request) {
        return authService.refresh(request)
                .map(res -> ResponseEntity.ok(responseWrapper.setData(res)));
    }

}
