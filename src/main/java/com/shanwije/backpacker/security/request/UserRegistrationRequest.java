package com.shanwije.backpacker.security.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    @NotBlank(message = "username cannot be empty")
    @Pattern(regexp = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$", message = "Invalid username")
    @Size(message = "Username must be between 6 and 25 characters", min = 6, max = 25)
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,50}$", message = "Invalid password")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "First name cannot be empty")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Invalid First Name")
    @Size(message = "First name must be between 2 and 25 characters", min = 2, max = 25)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Invalid Last Name")
    @Size(message = "Last name must be between 2 and 25 characters", min = 2, max = 25)
    private String lastName;

//    private List<String> authorities; //NOSONAR
}
