package com.example.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter
    extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
  Environment env;

  public AuthorizationHeaderFilter(Environment env) {
    super(Config.class);
    this.env = env;
  }

  // login -> token -> users (with token)
  @Override
  public GatewayFilter apply(Config config) {
    return ((exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();

      // 토큰이 잘 발급 되었는지 확인
      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
        return onError(exchange, "no auth header", HttpStatus.UNAUTHORIZED);

      String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String jwt = authorizationHeader.replace("Bearer", "");

      if (!isJwtValid(jwt))
        return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);

      return chain.filter(exchange);
    });
  }

  private boolean isJwtValid(String jwt) {
    boolean returnValue = true;
    String subject = null;
    try {
      subject =
          Jwts.parser()
              .setSigningKey(env.getProperty("token.secret"))
              .parseClaimsJws(jwt)
              .getBody()
              .getSubject();
    } catch (Exception ex) {
      returnValue = false;
    }
    if (subject == null || subject.isEmpty()) returnValue = false;

    return returnValue;
  }

  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    // webflux 안에서 처리할 수 있는 데이터 단위 Mono
    // 단위 값이 아니라면 flux를 사용할 수 있다 -> spring 5
    ServerHttpResponse response = exchange.getResponse(); // webflux 에서는 servlet을 더이상 사용하지 않음
    response.setStatusCode(httpStatus);
    log.error(err);
    return response.setComplete();
  }

  public static class Config {}
}
