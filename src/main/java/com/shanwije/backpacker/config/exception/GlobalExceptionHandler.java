/*
package com.shanwije.backpacker.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Autowired
    private DataBufferWriter bufferWriter;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        AppError appError = ErrorCode.GENERIC.toAppError();

        if (ex instanceof AppException) {
            AppException ae = (AppException) ex;
            status = ae.getStatusCode();
            appError = new AppError(ae.getCode(), ae.getText());

            log.debug(appError.toString());
        } else {
            log.error(ex.getMessage(), ex);
        }

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        exchange.getResponse().setStatusCode(status);
        return bufferWriter.write(exchange.getResponse(), appError);
    }
}
*/
