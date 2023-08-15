package com.example.userservice.security;

import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      log.info(request.getInputStream().toString());
      RequestLogin cred =
          new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
      log.info("cred 정보 : " + cred.toString());

      // 인증 정보 만들기
      return getAuthenticationManager()
          .authenticate(
              new UsernamePasswordAuthenticationToken(
                  cred.getEmail(), cred.getPassword(), new ArrayList<>()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
  }
}
