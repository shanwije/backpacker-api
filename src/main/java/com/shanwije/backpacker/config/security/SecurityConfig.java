package com.shanwije.backpacker.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.basic.auth.username}")
    String username;
    @Value("${spring.security.basic.auth.password}")
    CharSequence password;
    @Value("${spring.security.basic.auth.roles}")
    String roles;

    @Bean
    public MapReactiveUserDetailsService userDetailsService(){

        UserDetails userDetails = User.withUsername(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password))
                .roles(roles)
                .build();

        return new MapReactiveUserDetailsService(userDetails);
    }


    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(
                authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().authenticated()
        ).exceptionHandling()
                .authenticationEntryPoint((res, err) -> Mono.fromRunnable(()-> {
                    res.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((res,err) -> Mono.fromRunnable(() -> {
                    res.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                })).and()
                .httpBasic((Customizer.withDefaults()))
                .formLogin().disable()
                .csrf().disable()
                .build();
    }
}
