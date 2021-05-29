package com.shanwije.backpacker.security.controller;

import com.shanwije.backpacker.config.core.ResponseWrapper;
import com.shanwije.backpacker.security.request.ExisitingRequest;
import com.shanwije.backpacker.security.request.SignInRequest;
import com.shanwije.backpacker.security.request.SignUpRequest;
import com.shanwije.backpacker.security.request.TokenRequest;
import com.shanwije.backpacker.security.response.TokenResponse;
import com.shanwije.backpacker.security.service.AuthService;
import com.shanwije.backpacker.security.service.ValidationService;
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
@RequestMapping(value = "/validation", produces = MediaType.APPLICATION_JSON_VALUE)
public class ValidationController {

    ValidationService service;
    ResponseWrapper<Object> responseWrapper;

    @RequestMapping(value = "/is-unique", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<ResponseWrapper>> isUnique(@Valid @RequestBody ExisitingRequest request) {
        return service.isUnique(request)
                .map(res -> ResponseEntity.ok(responseWrapper.setData(res)));
    }

}
