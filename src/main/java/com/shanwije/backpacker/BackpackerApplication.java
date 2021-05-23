package com.shanwije.backpacker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableWebFlux
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
