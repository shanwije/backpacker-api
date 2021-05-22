package com.shanwije.backpacker.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MultiDataResponse<T extends Object> {
    List<T> data;

    public List<T> getData() {
        return data;
    }

    public MultiDataResponse setData(List<T> data) {
        this.data = data;
        return this;
    }
}
