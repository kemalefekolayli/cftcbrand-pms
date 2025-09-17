package com.example.cftcbrandtech.User.Dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class UserRegisDto {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;

}
