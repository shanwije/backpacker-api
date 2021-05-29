package com.shanwije.backpacker.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExisitingRequest {

    @NotBlank(message = "Value can not be blank")
    private String value;

    @NotBlank(message = "Type can not be blank")
    private String type;
}
