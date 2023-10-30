package com.example.commerce.service;

import com.example.commerce.common.MailComponent;
import com.example.commerce.domain.User;
import com.example.commerce.exception.impl.AlreadyExistsUserEmailException;
import com.example.commerce.exception.impl.AlreadyExistsUsernameException;
import com.example.commerce.exception.impl.EmailDoesNotAuthenticatedException;
import com.example.commerce.exception.impl.UserNotFoundException;
import com.example.commerce.exception.impl.UsernamePasswordNotMatchException;
import com.example.commerce.model.LoginUser;
import com.example.commerce.model.RegisterUser;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TokenProvider tokenProvider;
  private final MailComponent mailComponent;

  // 이메일 및 아이디 중복 체크 후 비밀번호를 암호화하여 저장
  public RegisterUser.Response registerUser(RegisterUser.Request registerForm) {
    userEmailDuplicatedCheck(registerForm.getEmail());

    usernameDuplicatedCheck(registerForm.getUsername());

    registerForm.setPassword(passwordEncoder.encode(registerForm.getPassword()));

    User user = userRepository.save(User.builder()
        .email(registerForm.getEmail())
        .username(registerForm.getUsername())
        .password(registerForm.getPassword())
        .emailVerification(false)
        .roles(registerForm.getRole())
        .build());

    mailComponent.registerVerifyEmailSend(user.getId(), user.getEmail(), user.getUsername());

    return RegisterUser.Response.builder()
        .username(user.getUsername())
        .registeredAt(user.getCreatedAt())
        .build();
  }

  // 이메일 중복 체크
  private void userEmailDuplicatedCheck(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new AlreadyExistsUserEmailException();
    }
  }

  // 아이디 중복 체크
  private void usernameDuplicatedCheck(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new AlreadyExistsUsernameException();
    }
  }

  // 비밀번호 일치 확인 및 이메일 인증 여부 확인 후 토큰 발급
  public LoginUser.Response loginUser(LoginUser.Request loginForm) {
    User user = userRepository.findByUsername(loginForm.getUsername())
        .orElseThrow(UsernamePasswordNotMatchException::new);

    if (!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
      throw new UsernamePasswordNotMatchException();
    }

    if (!user.isEmailVerification()) {
      throw new EmailDoesNotAuthenticatedException();
    }

    String token = tokenProvider.generateToken(user.getUsername(), user.getRoles());

    return LoginUser.Response.builder()
        .username(user.getUsername())
        .token(token)
        .build();
  }

  // 이메일 인증
  public String verifyEmail(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);

    user.setEmailVerification(true);
    userRepository.save(user);

    return user.getUsername() + "님의 이메일 인증이 완료되었습니다.";
  }
}