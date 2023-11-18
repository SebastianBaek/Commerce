package com.example.commerce.controller;

import com.example.commerce.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

  private final CouponService couponService;

  @GetMapping("/getAll")
  @PreAuthorize("hasRole('COMMON')")
  public ResponseEntity<?> getAllCoupon(Authentication authentication) {
    return ResponseEntity.ok(couponService.getAllCoupon(authentication.getName()));
  }
}
