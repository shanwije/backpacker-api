package com.shanwije.backpacker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableWebFlux
public class BackpackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackpackerApplication.class, args);
    }

//    @GetMapping("/auth-client")
//    public Object getAuthorizedClient(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client){
//        System.out.println(client);
//        return client;
//    }
//
//    @GetMapping("/auth-user")
//    public Object getUser(@AuthenticationPrincipal OAuth2User user){
//        System.out.println(user);
//        return user;
//    }
}
