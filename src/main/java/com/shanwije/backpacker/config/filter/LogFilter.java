package com.shanwije.backpacker.config.filter;

import com.shanwije.backpacker.config.core.ConcurrentContext;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.UUID;


@Log4j2
@Component
@AllArgsConstructor
public class LogFilter implements WebFilter {

    public static final String CORRELATION_ID_KEY = "Correlation_Id";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

//        List<String> correlationIds = serverWebExchange.getRequest().getHeaders().get(CORRELATION_ID_HEADER); //NOSONAR

        var correlationId = UUID.randomUUID().toString();

        var req = serverWebExchange.getRequest();

        log.info("Request recieved, path: {}, Http method : {}, Headers : {}, Body: {}", req.getPath(), req.getMethod(), req.getHeaders(), req.getBody());
        log.info("setting co-relation ID: {}", correlationId);
        ConcurrentContext.put(CORRELATION_ID_KEY, correlationId);

        return webFilterChain.filter(serverWebExchange).subscriberContext((Context context) -> context.put(CORRELATION_ID_KEY, correlationId));
    }
}
