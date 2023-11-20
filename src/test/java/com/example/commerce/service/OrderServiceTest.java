package com.example.commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.commerce.domain.Order;
import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.OrderProduct;
import com.example.commerce.repository.CouponRepository;
import com.example.commerce.repository.OrderRepository;
import com.example.commerce.repository.ProductRepository;
import com.example.commerce.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CouponRepository couponRepository;

  @InjectMocks
  private OrderService orderService;

  @Test
  @DisplayName("회원의 주문 성공 테스트")
  void orderProductSuccess() {
    //given
    OrderProduct.Request request = OrderProduct.Request.builder()
        .productId(1L)
        .amount(1L)
        .address("서울시")
        .build();

    Product apple = Product.builder()
        .productName("사과")
        .price(1000L)
        .amount(100L)
        .maker("의성")
        .rating(null)
        .sales(0L)
        .build();

    User user = User.builder()
        .email("test@naver.com")
        .username("홍길동")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_SELLER"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    Order order = Order.builder()
        .user(user)
        .product(apple)
        .amount(1L)
        .address("서울시")
        .build();

    given(productRepository.findById(anyLong()))
        .willReturn(Optional.of(apple));
    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.of(user));
    given(orderRepository.save(any()))
        .willReturn(order);
    //when
    OrderProduct.Response response = orderService.orderProduct(request, "홍길동");
    //then
    assertEquals("사과", response.getProductName());
  }

  @Test
  @DisplayName("회원의 주문시 상품 찾기 실패 테스트")
  void orderProductNotFoundFail() {
    //given
    OrderProduct.Request request = OrderProduct.Request.builder()
        .productId(1L)
        .amount(1L)
        .address("서울시")
        .build();

    given(productRepository.findById(anyLong()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> orderService.orderProduct(request, "홍길동"));
    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("회원의 주문시 회원 찾기 실패 테스트")
  void orderProductUserNotFoundFail() {
    //given
    OrderProduct.Request request = OrderProduct.Request.builder()
        .productId(1L)
        .amount(1L)
        .address("서울시")
        .build();

    Product apple = Product.builder()
        .productName("사과")
        .price(1000L)
        .amount(100L)
        .maker("의성")
        .rating(null)
        .sales(0L)
        .build();

    given(productRepository.findById(anyLong()))
        .willReturn(Optional.of(apple));
    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> orderService.orderProduct(request, "홍길동"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

}