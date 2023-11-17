package com.example.commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.commerce.domain.Cart;
import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.CartInfo;
import com.example.commerce.repository.CartRepository;
import com.example.commerce.repository.ProductRepository;
import com.example.commerce.repository.UserRepository;
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
class CartServiceTest {

  @Mock
  private CartRepository cartRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CartService cartService;

  @Test
  @DisplayName("회원의 장바구니 담기 성공 테스트")
  void addProductSuccess() {
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
        .willReturn(Optional.of(user));
    //when
    cartService.addProduct(1L, user.getUsername());
    //then
    verify(cartRepository).save(any());
  }

  @Test
  @DisplayName("회원의 장바구니 담기시 상품 찾기 실패 테스트")
  void addProductNotFoundFail() {
    //given
    given(productRepository.findById(anyLong()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> cartService.addProduct(1L, "홍길동"));
    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("회원의 장바구니 담기시 회원 찾기 실패 테스트")
  void addProductUserNotFoundFail() {
    //given
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
        CustomException.class, () -> cartService.addProduct(1L, "홍길동"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("회원의 장바구니 상품 불러오기 성공 테스트")
  void getProductsSuccess() {
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

    Product apple = Product.builder()
        .productName("사과")
        .price(1000L)
        .amount(100L)
        .maker("의성")
        .rating(null)
        .sales(0L)
        .build();
    Product pear = Product.builder()
        .productName("배")
        .price(1000L)
        .amount(100L)
        .maker("나주")
        .rating(null)
        .sales(0L)
        .build();

    Cart cart1 = Cart.builder()
        .user(user)
        .product(apple)
        .amount(1L)
        .build();
    Cart cart2 = Cart.builder()
        .user(user)
        .product(pear)
        .amount(1L)
        .build();

    List<Cart> carts = new ArrayList<>();
    carts.add(cart1);
    carts.add(cart2);

    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.of(user));
    given(cartRepository.findByUser(any()))
        .willReturn(Optional.of(carts));
    //when
    List<CartInfo> response = cartService.getProducts("홍길동");
    //then
    assertEquals("사과", response.get(0).getProductName());
    assertEquals("배", response.get(1).getProductName());
  }

  @Test
  @DisplayName("회원의 장바구니 상품 불러올 시 회원 찾기 실패 테스트")
  void getProductsUserNotFoundFail() {
    //given
    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> cartService.getProducts("홍길동"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("회원의 장바구니 상품 불러올 시 장바구니의 상품 찾기 실패 테스트")
  void getProductsCartNotFoundFail() {
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
    given(cartRepository.findByUser(any()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> cartService.getProducts("홍길동"));
    //then
    assertEquals(ErrorCode.CART_NOT_FOUND, e.getErrorCode());
  }
}