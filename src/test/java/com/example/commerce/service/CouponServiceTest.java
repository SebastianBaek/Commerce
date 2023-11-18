package com.example.commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.commerce.domain.Coupon;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.CouponInfo;
import com.example.commerce.repository.CouponRepository;
import com.example.commerce.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

  @Mock
  private CouponRepository couponRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CouponService couponService;

  @Test
  @DisplayName("회원의 쿠폰 불러오기 성공 테스트")
  void getAllCouponSuccess() {
    //given
    User user = User.builder()
        .email("test@naver.com")
        .username("홍길동")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_COMMON"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    Coupon coupon1 = Coupon.builder()
        .user(user)
        .rate(10)
        .expiration(LocalDate.now().plusDays(30))
        .build();
    Coupon coupon2 = Coupon.builder()
        .user(user)
        .saving(5000)
        .expiration(LocalDate.now().plusDays(30))
        .build();
    List<Coupon> coupons = new ArrayList<>();
    coupons.add(coupon1);
    coupons.add(coupon2);

    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.of(user));
    given(couponRepository.findByUser(any()))
        .willReturn(Optional.of(coupons));
    //when
    List<CouponInfo> couponInfos = couponService.getAllCoupon("홍길동");
    //then
    assertEquals(10, couponInfos.get(0).getRate());
    assertEquals(5000, couponInfos.get(1).getSaving());
  }

  @Test
  @DisplayName("회원의 쿠폰 불러올 시 회원 찾기 실패 테스트")
  void getAllCouponUserNotFoundFail() {
    //given
    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> couponService.getAllCoupon("홍길동"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("회원의 쿠폰 불러올 시 쿠폰 찾기 실패 테스트")
  void getAllCouponNotFoundFail() {
    //given
    User user = User.builder()
        .email("test@naver.com")
        .username("홍길동")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_COMMON"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.of(user));
    given(couponRepository.findByUser(any()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> couponService.getAllCoupon("홍길동"));
    //then
    assertEquals(ErrorCode.COUPON_NOT_FOUND, e.getErrorCode());
  }

}