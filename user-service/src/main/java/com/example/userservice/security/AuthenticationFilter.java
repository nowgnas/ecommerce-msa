package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private UserService userService;
  private Environment env;

  public AuthenticationFilter(
      AuthenticationManager authenticationManager, UserService userService, Environment env) {
    super(authenticationManager);
    this.userService = userService;
    this.env = env;
  }

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
    //    super.successfulAuthentication(request, response, chain, authResult);
    String userName = ((User) authResult.getPrincipal()).getUsername();
    UserDto userDetails = userService.getUserDetailsByEmail(userName);

    String token =
        Jwts.builder()
            .setSubject(userDetails.getUserId())
            .setExpiration(
                new Date(
                    System.currentTimeMillis()
                        + Long.parseLong(env.getProperty("token.expiration_time"))))
            .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
            .compact();

    response.addHeader("token", token);
    response.addHeader("userId", userDetails.getUserId());
  }
}
