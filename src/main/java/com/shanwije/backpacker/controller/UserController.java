package com.shanwije.backpacker.controller;

import com.shanwije.backpacker.entities.User;
import com.shanwije.backpacker.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.awt.*;

@Log4j2
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public Publisher<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Publisher<User> getById(@PathVariable(value = "id")String id) {
        return userService.findById(id);
    }

    @PostMapping
    public Publisher<User> create(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping
    public Publisher<User> deleteById(@PathVariable(value = "id")String id) {
        return userService.delete(id);
    }

    @PutMapping
    public Publisher<User> updateUser(@RequestBody User user) {
        return userService.save(user);
    }
}
