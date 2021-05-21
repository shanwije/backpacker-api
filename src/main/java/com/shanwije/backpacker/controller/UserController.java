package com.shanwije.backpacker.controller;

import com.shanwije.backpacker.security.AuthService;
import com.shanwije.backpacker.security.request.UserAuthenticationRequest;
import com.shanwije.backpacker.security.request.UserRegistrationRequest;
import com.shanwije.backpacker.security.response.TokenAuthenticationResponse;
import com.shanwije.backpacker.security.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    //    final UserService userService;
//    private UserRepository userRepository;
//    private JWTUtil jwtUtil;
    AuthService authService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return authService.register(userRegistrationRequest);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Mono<TokenAuthenticationResponse> getToken(@RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        return authService.getToken(userAuthenticationRequest);
    }


/*    @GetMapping()
    public Flux<User> getAll() {
        return userService.findAll();
    }

    @PostMapping("/token")
    public Mono<ResponseEntity<?>> login(@RequestBody User user) {
        return userRepository.findByUsername(user.getUsername()).map((userDetails) -> {
            if (getPasswordEncoder().matches(user.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.ok(jwtUtil.generateToken(user));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getById(@PathVariable(value = "id")String id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<User> create(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping
    public Mono<ResponseEntity<User>> deleteById(@PathVariable(value = "id")String id) {
        return userService.delete(id).map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping
    public Mono<ResponseEntity<User>> updateUser(@PathVariable(value = "id")String id,
                                      @RequestBody User user) {
        return userService.findById(id)
                .flatMap(user1 -> {
                    user1.setUsername(user.getUsername());
                    user1.setPassword(user.getPassword());
                    user1.setRoles(user.getRoles());
                    return userService.save(user1);
                })
                .map(updatedUser -> new ResponseEntity<>(updatedUser, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/
}
