package com.example.userservice.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLogin {
  @NotNull(message = "email cannot be null")
  @Size(min = 2, message = "email not be less than 2")
  @Email
  private String email;

  @NotNull(message = "pass cannot be null")
  @Size(min = 2, message = "password cannot be less than 2")
  private String password;
}
