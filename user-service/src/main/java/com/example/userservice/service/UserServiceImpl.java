package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  UserRepository userRepository;
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    // UserServiceApplication 에 빈으로 등록해서 동작하도록 한다
    this.passwordEncoder = passwordEncoder; // 어디에서도 생성한 적이 없기 때문에 가장 먼저 호출 되는 클래스에서 넣어줘야 한다
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(username);
    if (userEntity == null) {
      throw new UsernameNotFoundException(username);
    }
    // credential 의 기본 클래스
    return new User(
        userEntity.getEmail(),
        userEntity.getEncryptedPwd(),
        true,
        true,
        true,
        true,
        new ArrayList<>());
  }

  @Override
  public UserDto createUser(UserDto userDto) {
    userDto.setUserId(UUID.randomUUID().toString());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);
    userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
    userRepository.save(userEntity);
    return mapper.map(userEntity, UserDto.class);
  }

  @Override
  public UserDto getUserByUserId(String userId) {
    UserEntity user = userRepository.findByUserId(userId);
    if (user == null) throw new UsernameNotFoundException("USER NOT FOUND");

    UserDto userDto = new ModelMapper().map(user, UserDto.class);
    List<ResponseOrder> orders = new ArrayList<>();
    userDto.setOrders(orders);

    return userDto;
  }

  @Override
  public Iterable<UserEntity> getUserByAll() {
    return userRepository.findAll();
  }

  @Override
  public UserDto getUserDetailsByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email);

    if (userEntity == null) throw new UsernameNotFoundException(email);

    return new ModelMapper().map(userEntity, UserDto.class);
  }
}
