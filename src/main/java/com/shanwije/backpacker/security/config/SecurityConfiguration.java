package com.shanwije.backpacker.security.config;

import com.shanwije.backpacker.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return CustomPasswordEncoder.getPasswordEncoder();
    }


    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(
                        authorizeExchangeSpec -> authorizeExchangeSpec
//                                .pathMatchers("/**").permitAll()
                                .pathMatchers("/").permitAll()
                                .pathMatchers("/version").permitAll()
                                .pathMatchers("/health").permitAll()
                                .pathMatchers("/health/**").permitAll()
                                .pathMatchers("/actuator/**").permitAll()
                                .pathMatchers("/swagger-resources/**").permitAll()
                                .pathMatchers("/swagger-ui/**").permitAll()
                                .pathMatchers("/v2/api-docs").permitAll()
                                .pathMatchers("/roles").permitAll()
                                .pathMatchers("/register").permitAll()
                                .pathMatchers("/authenticate").permitAll()
                                .pathMatchers("/**").authenticated()
                ).exceptionHandling()
                .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.BAD_REQUEST))
                .authenticationEntryPoint((res, err) ->
                        Mono.fromRunnable(() ->
                                res.getResponse()
                                        .setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler((res, err) -> Mono.fromRunnable(() ->
                        res.getResponse()
                                .setStatusCode(HttpStatus.FORBIDDEN)))
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .anonymous().disable()
                .csrf()
                .disable()
                .headers()
                .frameOptions().disable()
                .cache().disable()
                .and()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .requestCache().requestCache(NoOpServerRequestCache.getInstance())
                .and()
                .build();
    }

}
