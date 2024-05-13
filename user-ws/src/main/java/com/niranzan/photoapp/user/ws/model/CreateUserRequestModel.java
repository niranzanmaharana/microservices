package com.niranzan.photoapp.user.ws.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequestModel {
    @NotEmpty(message = "First name can not be empty!")
    @Size(min = 2, message = "First name must not be less than 2 chars!")
    private String firstName;
    @NotEmpty(message = "Last name can not be empty!")
    @Size(min = 2, message = "Last name must not be less than 2 chars!")
    private String lastName;
    @NotEmpty(message = "Password can not be empty!")
    @Size(min = 8, max = 16, message = "Password must not be between 8 to 16 chars!")
    private String password;
    @NotEmpty(message = "Email can not be empty!")
    @Email(message = "Invalid email!")
    private String email;
}
