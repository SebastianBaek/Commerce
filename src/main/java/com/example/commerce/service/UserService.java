package com.example.commerce.service;

import com.example.commerce.common.MailComponent;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.FindPasswordRequest;
import com.example.commerce.model.FindUsernameRequest;
import com.example.commerce.model.LoginUser;
import com.example.commerce.model.RegisterUser;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.security.TokenProvider;
import java.util.UUID;
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

    mailComponent.sendVerifyLink(user.getId(), user.getEmail(), user.getUsername());

    return RegisterUser.Response.builder()
        .username(user.getUsername())
        .createdAt(user.getCreatedAt())
        .build();
  }

  // 이메일 중복 체크
  private void userEmailDuplicatedCheck(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new CustomException(ErrorCode.ALREADY_EXISTS_USER_EMAIL);
    }
  }

  // 아이디 중복 체크
  private void usernameDuplicatedCheck(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new CustomException(ErrorCode.ALREADY_EXISTS_USERNAME);
    }
  }

  // 비밀번호 일치 확인 및 이메일 인증 여부 확인 후 토큰 발급
  public LoginUser.Response loginUser(LoginUser.Request loginForm) {
    User user = userRepository.findByUsername(loginForm.getUsername())
        .orElseThrow(() -> new CustomException(ErrorCode.USERNAME_AND_PASSWORD_NOT_MATCH));

    if (!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.USERNAME_AND_PASSWORD_NOT_MATCH);
    }

    if (!user.isEmailVerification()) {
      throw new CustomException(ErrorCode.EMAIL_IS_NOT_VERIFIED);
    }

    String token = tokenProvider.generateToken(user.getUsername(), user.getRoles());

    return LoginUser.Response.builder()
        .username(user.getUsername())
        .token(token)
        .build();
  }

  // 이메일 인증
  public String verifyUserEmail(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.setEmailVerification(true);
    userRepository.save(user);

    return user.getUsername() + "님의 이메일 인증이 완료되었습니다.";
  }

  public String findUsername(FindUsernameRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    mailComponent.sendUsername(user.getEmail(), user.getUsername());

    return user.getEmail() + "로 아이디 전송이 완료되었습니다.";
  }

  public String findPassword(FindPasswordRequest request) {
    String email = request.getEmail();
    String username = request.getUsername();

    User user = userRepository.findByEmailAndUsername(email, username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    String temporaryPassword = UUID.randomUUID().toString().replace("-", "");
    user.setPassword(passwordEncoder.encode(temporaryPassword));
    userRepository.save(user);

    mailComponent.sendTemporaryPassword(user.getEmail(), user.getUsername(), temporaryPassword);

    return user.getEmail() + "로 임시 비밀번호 전송이 완료되었습니다.";
  }
}