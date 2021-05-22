/*
package com.shanwije.backpacker.config.functional;

import com.shanwije.backpacker.entities.User;
import com.shanwije.backpacker.security.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserHandler {

    private UserService userService;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        Flux<User> userFlux = userService.findAll();
        return ServerResponse.ok().body(userFlux, User.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String userId = serverRequest.pathVariable("id");

        return userService.findById(userId)
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<User> userMono = serverRequest.bodyToMono(User.class)
                .flatMap(userService::save);

        return ServerResponse.status(HttpStatus.CREATED).body(userMono, User.class);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest){
        String userId = serverRequest.pathVariable("id");
        return userService.findById(userId)
                .flatMap(user -> {
                    Mono<User> updated = serverRequest.bodyToMono(User.class)
                            .flatMap(userService::save);
                    return ServerResponse.ok().body(updated, User.class);
                }).switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String userId = serverRequest.pathVariable("id");

        return userService.findById(userId)
                .flatMap(user -> {
                    userService.delete(user.getId());
                    return ServerResponse.ok().body(user, User.class);
                }).switchIfEmpty(ServerResponse.notFound().build());
    }

}
*/
