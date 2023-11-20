package com.example.commerce.controller;

import com.example.commerce.model.OrderProduct;
import com.example.commerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @PreAuthorize("hasRole('COMMON')")
  public ResponseEntity<?> orderProduct(
      @RequestBody OrderProduct.Request request, Authentication authentication) {
    return ResponseEntity.ok(orderService.orderProduct(request, authentication.getName()));
  }
}
