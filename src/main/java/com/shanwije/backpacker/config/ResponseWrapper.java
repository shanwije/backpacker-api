package com.shanwije.backpacker.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ResponseWrapper<T extends Object> {
    private T data;

    public T getData() {
        return data;
    }

    public ResponseWrapper setData(T data) {
        this.data = data;
        return this;
    }
}
