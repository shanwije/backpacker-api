package com.shanwije.backpacker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestTimingFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startMillis = System.currentTimeMillis();
        return chain.filter(exchange)
                .doOnSuccess(aVoid ->
                        log.info("Request elapsed Time: {}ms", System.currentTimeMillis() - startMillis)
                );
    }
}
