/*
package com.shanwije.backpacker.config.functional;

import com.shanwije.backpacker.config.functional.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> root(UserHandler userHandler){
        return RouterFunctions.route()
                .GET("/user", userHandler::findAll)
                .GET("/user/{id}", userHandler::findById)
                .POST("/user", userHandler::save)
                .PUT("/user/{id}", userHandler::update)
                .DELETE("/user/{id}", userHandler::delete)
                .build();
    }
}
*/
