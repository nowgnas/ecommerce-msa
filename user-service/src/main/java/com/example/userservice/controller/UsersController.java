package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-service")
@Slf4j
public class UsersController {
  private final UserService userService;
  private final Environment env;
  @Autowired private Greeting greeting;

  @Autowired
  public UsersController(UserService userService, Environment env) {
    this.userService = userService;
    this.env = env;
  }

  @GetMapping("/health_check")
  public String status() {
    return String.format("it's working on PORT %s", env.getProperty("local.server.port"));
  }

  @GetMapping("welcome")
  public String welcome() {
    //        return env.getProperty("greeting.message");
    return greeting.getMessage();
  }

  @PostMapping("users")
  public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
    log.info(user.toString());
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    UserDto userDto = mapper.map(user, UserDto.class);
    userService.createUser(userDto);

    ResponseUser user1 = mapper.map(userDto, ResponseUser.class);

    return ResponseEntity.status(HttpStatus.CREATED).body(user1); // 성곰 메세지 전달
  }

  @GetMapping("users")
  public ResponseEntity<List<ResponseUser>> getUsers() {
    Iterable<UserEntity> userByAll = userService.getUserByAll();
    List<ResponseUser> result = new ArrayList<>();

    userByAll.forEach(
        v -> {
          result.add(new ModelMapper().map(v, ResponseUser.class));
        });
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("users/{userId}")
  public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
    UserDto userDto = userService.getUserByUserId(userId);
    ResponseUser map = new ModelMapper().map(userDto, ResponseUser.class);
    return ResponseEntity.status(HttpStatus.OK).body(map);
  }
}
