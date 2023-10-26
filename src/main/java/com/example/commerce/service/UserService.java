package com.example.commerce.service;

import com.example.commerce.domain.User;
import com.example.commerce.exception.impl.AlreadyExistsUserEmailException;
import com.example.commerce.exception.impl.AlreadyExistsUsernameException;
import com.example.commerce.model.RegisterUser;
import com.example.commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("회원이 존재하지 않습니다."));
  }

  public RegisterUser.Response registerUser(RegisterUser.Request registerForm) {
    userEmailDuplicatedCheck(registerForm.getEmail());

    usernameDuplicatedCheck(registerForm.getUsername());

    registerForm.setPassword(passwordEncoder.encode(registerForm.getPassword()));

    User user = userRepository.save(User.builder()
        .email(registerForm.getEmail())
        .username(registerForm.getUsername())
        .password(registerForm.getPassword())
        .roles(registerForm.getRole())
        .build());

    return RegisterUser.Response.builder()
        .username(user.getUsername())
        .registeredAt(user.getCreatedAt())
        .build();
  }

  private void userEmailDuplicatedCheck(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new AlreadyExistsUserEmailException();
    }
  }

  private void usernameDuplicatedCheck(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new AlreadyExistsUsernameException();
    }
  }
}