package com.example.commerce.service;

import com.example.commerce.domain.Coupon;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.CouponInfo;
import com.example.commerce.repository.CouponRepository;
import com.example.commerce.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;
  private final UserRepository userRepository;

  public List<CouponInfo> getAllCoupon(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    List<Coupon> coupons = couponRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

    return coupons.stream().map(coupon -> CouponInfo.builder()
        .rate(coupon.getRate())
        .saving(coupon.getSaving())
        .expiration(coupon.getExpiration())
        .build()).collect(Collectors.toList());
  }
}
