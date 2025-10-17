package com.bash.boundbackend.modules.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegistrationRequest {


    @NotBlank( message = "Firstname required")
    private String firstname;


    @NotBlank( message = "Lastname required")
    private String lastname;


    @NotBlank( message = "Email required")
    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 8, message = "Required password must be 8 characters long")
    @NotBlank( message = "Password required")
    private String password;

}
