package com.example.userservice.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ResponseDto<D> {
  private String message;
  private String statusCode;
  private D data;

  public ResponseDto(String message, String statusCode, D data) {
    this.message = message;
    this.statusCode = statusCode;
    this.data = data;
  }

  public static <D> ResponseDto<D> ofSuccess() {
    return new ResponseDto<>("success", null, null);
  }

  public static Object of(String a, String b, String c) {
    return null;
  }

}
