package com.example.commerce.controller;

import com.example.commerce.model.LoginUser;
import com.example.commerce.model.RegisterUser;
import com.example.commerce.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  // 유저 회원가입
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUser.Request registerForm) {
    return ResponseEntity.ok(userService.registerUser(registerForm));
  }

  // 유저 로그인
  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody @Valid LoginUser.Request loginForm) {
    return ResponseEntity.ok(userService.loginUser(loginForm));
  }

  // 회원가입시 이메일 인증
  @GetMapping("/verify/{id}")
  public ResponseEntity<?> verifyUserEmail(@PathVariable Long id) {
    return ResponseEntity.ok(userService.verifyEmail(id));
  }
}
