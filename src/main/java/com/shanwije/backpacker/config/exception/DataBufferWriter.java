package com.shanwije.backpacker.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class DataBufferWriter {
    private final ObjectMapper objectMapper;

    public <T> Mono<Void> write(ServerHttpResponse httpResponse, T object) {
        return httpResponse
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = httpResponse.bufferFactory();
                    try {
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(object));
                    } catch (Exception ex) {
                        log.warn("Error writing response", ex);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}
