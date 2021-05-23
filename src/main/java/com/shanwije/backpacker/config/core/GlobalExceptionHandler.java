package com.shanwije.backpacker.config.core;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

import static org.springframework.boot.autoconfigure.web.WebProperties.Resources;


@Component
@Log4j2
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    ResponseWrapper<Object> responseWrapper = new ResponseWrapper<>();

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, Resources resources, ApplicationContext applicationContext,
                                  ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), request -> formatErrorResponse(request));
    }

    private Mono<ServerResponse> formatErrorResponse(ServerRequest request) {
        Map<String, Object> errorStringObjectMap =
                getErrorAttributes(request, ErrorAttributeOptions
                        .of(ErrorAttributeOptions.Include.EXCEPTION)
                        .including(ErrorAttributeOptions.Include.MESSAGE)
                        .including(ErrorAttributeOptions.Include.STACK_TRACE)
                );
        int status = ((int) Optional.ofNullable(errorStringObjectMap.get("status")).orElse(HttpStatus.INTERNAL_SERVER_ERROR.value()));

        var exception = errorStringObjectMap.get("exception").toString();

        if (AuthenticationCredentialsNotFoundException.class.getName().equals(exception)

                || SignatureException.class.getName().equals(exception)
                || MalformedJwtException.class.getName().equals(exception)

                || BadCredentialsException.class.getName().equals(exception)
                || DisabledException.class.getName().equals(exception)
                || LockedException.class.getName().equals(exception)
                || AccountExpiredException.class.getName().equals(exception)) {
            status = HttpStatus.UNAUTHORIZED.value();
        } else if (AccessDeniedException.class.getName().equals(exception)) {
            status = HttpStatus.FORBIDDEN.value();
        }

        log.error("Error {}", errorStringObjectMap);

        errorStringObjectMap.remove("exception");
        errorStringObjectMap.remove("status");
        errorStringObjectMap.remove("error");
        errorStringObjectMap.remove("error");
        errorStringObjectMap.remove("trace");

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseWrapper.setData(errorStringObjectMap)));
    }
}
