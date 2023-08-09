package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {
    @NotNull(message = "Email cannot null")
    @Size(min = 2, message = "email not be less then two char")
    private String email;
    @NotNull(message = "name cannot be null")
    @Size(min = 2)
    private String name;
    @NotNull(message = "password cannot be null")
    @Size(min = 2)
    private String pwd;
}
