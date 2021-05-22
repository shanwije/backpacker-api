package com.shanwije.backpacker.config;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.UUID;


@Log4j2
@Component
@AllArgsConstructor
public class LogFilter implements WebFilter {

    public static final String CORRELATION_ID_KEY = "Correlation_Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

//        List<String> correlationIds = serverWebExchange.getRequest().getHeaders().get(CORRELATION_ID_HEADER);

        var correlationId = UUID.randomUUID().toString();

        log.info("setting co-relation ID: {}", correlationId);
        ConcurrentContext.put(CORRELATION_ID_KEY, correlationId);

        return webFilterChain.filter(serverWebExchange).subscriberContext((Context context) -> context.put(CORRELATION_ID_KEY, correlationId));
    }
}
