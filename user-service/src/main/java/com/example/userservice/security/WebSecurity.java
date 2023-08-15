package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 다른 빈들 보다 우선적으로 실행 된다
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
  private UserService userService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private Environment env;

  public WebSecurity(
      Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.env = env;
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    //        http.authorizeRequests().antMatchers("/users/**").permitAll();

    // 모든 요청에 대해 막기
    http.authorizeRequests()
        .antMatchers("/**")
        .hasIpAddress("localhost")
        .and()
        .addFilter(getAuthenticationFilter());

    http.headers().frameOptions().disable();
  }

  private AuthenticationFilter getAuthenticationFilter() throws Exception {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter();
    authenticationFilter.setAuthenticationManager(authenticationManager());

    return authenticationFilter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // select pwd from users where email=?
    // db_pwd(encryped) == input pwd(encryped)
    auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
  }
}
