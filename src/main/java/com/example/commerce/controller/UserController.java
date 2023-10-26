package com.example.commerce.controller;

import com.example.commerce.model.RegisterUser;
import com.example.commerce.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
