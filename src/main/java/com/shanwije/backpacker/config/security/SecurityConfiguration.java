package com.shanwije.backpacker.config.security;

import com.shanwije.backpacker.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    ReactiveUserDetailsService userDetailsService(){
        return (name) -> userRepository.findByUsername(name);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(
                        authorizeExchangeSpec -> {
                            authorizeExchangeSpec
                                    .pathMatchers("/").permitAll()
                                    .pathMatchers("/version").permitAll()
                                    .pathMatchers("/health").permitAll()
                                    .pathMatchers("/health/**").permitAll()
                                    .pathMatchers("/actuator/**").permitAll()
                                    .pathMatchers("/swagger-resources/**").permitAll()
                                    .pathMatchers("/swagger-ui/**").permitAll()
                                    .pathMatchers("/v2/api-docs").permitAll()
                                    .pathMatchers("/user").permitAll()
                                    .pathMatchers("/**").authenticated();
                        }
                ).exceptionHandling()
                .authenticationEntryPoint((res, err) -> Mono.fromRunnable(() -> {
                    res.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((res, err) -> Mono.fromRunnable(() -> {
                    res.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                })).and()
                .httpBasic((Customizer.withDefaults()))
                .formLogin().disable()
                .csrf().disable()
                .build();
    }
}
