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

  @PostMapping("/register")
  public ResponseEntity<?> userRegister(@RequestBody @Valid RegisterUser.Request registerForm) {
    return ResponseEntity.ok(userService.registerUser(registerForm));
  }

  @PostMapping("/login")
  public ResponseEntity<?> userLogin(@RequestBody @Valid LoginUser.Request loginForm) {
    return ResponseEntity.ok(userService.loginUser(loginForm));
  }

  @GetMapping("/verify/{id}")
  public ResponseEntity<?> userEmailVerify(@PathVariable Long id) {
    return ResponseEntity.ok(userService.verifyEmail(id));
  }
}
