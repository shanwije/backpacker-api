package com.shanwije.backpacker.config.security;

import com.shanwije.backpacker.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;


    @Bean
    ReactiveUserDetailsService userDetailsService(){
        return (name) -> userRepository.findByUsername(name);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return CustomPasswordEncoder.getPasswordEncoder();
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
//                                    .pathMatchers("/user").permitAll()
                                    .pathMatchers("/user/token").permitAll()
                                    .pathMatchers("/**").authenticated();
                        }
                ).exceptionHandling()
                .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.BAD_REQUEST))
                .authenticationEntryPoint((res, err) -> Mono.fromRunnable(() -> {
                    res.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((res, err) -> Mono.fromRunnable(() -> {
                    res.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                })).and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .requestCache().requestCache(NoOpServerRequestCache.getInstance())
                .and()
                .build();
    }

}
