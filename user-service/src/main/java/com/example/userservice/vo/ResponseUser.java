package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
  private String email;
  private String name;
  private String userId;

  private List<ResponseOrder> orders;
}
