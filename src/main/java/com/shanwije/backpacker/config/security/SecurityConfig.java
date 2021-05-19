package com.shanwije.backpacker.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

//@EnableWebFluxSecurity
public class SecurityConfig {

//    @Bean
//    MapReactiveUserDetailsService userDetailsService(){
//        UserDetails rob = User.builder()
//        .username("shan@x.com")
//                .password("password")
//                .roles("USER")
//                .build();
//        return new MapReactiveUserDetailsService(rob);
//    }
}
