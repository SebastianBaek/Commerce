package com.example.commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.RegisterProduct;
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
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ProductService productService;

  @Test
  @DisplayName("상품 등록 성공 테스트")
  void registerProductSuccess() {
    //given
    RegisterProduct.Request request = RegisterProduct.Request.builder()
        .productName("상품명")
        .price(100L)
        .amount(1L)
        .maker("Commerce")
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

    Product product = Product.builder()
        .user(user)
        .productName(request.getProductName())
        .price(request.getPrice())
        .amount(request.getAmount())
        .maker(request.getMaker())
        .sales(0L)
        .build();
    product.setId(1L);

    given(productRepository.existsByProductName(anyString())).willReturn(false);
    given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
    given(productRepository.save(any())).willReturn(product);
    //when
    RegisterProduct.Response response = productService.registerProduct(request, user.getUsername());
    //then
    assertEquals(1L, response.getProductId());
    assertEquals("상품명", response.getProductName());
  }

  @Test
  @DisplayName("상품 등록시 상품명 중복 실패 테스트")
  void registerProductNameDuplicatedFail() {
    //given
    RegisterProduct.Request request = RegisterProduct.Request.builder()
        .productName("상품명")
        .price(100L)
        .amount(1L)
        .maker("Commerce")
        .build();

    given(productRepository.existsByProductName(anyString())).willReturn(true);
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> productService.registerProduct(request, "홍길동"));
    //then
    assertEquals(ErrorCode.ALREADY_EXISTS_PRODUCT_NAME, e.getErrorCode());
  }

  @Test
  @DisplayName("상품 등록시 회원 찾기 실패 테스트")
  void registerProductUserNotFoundFail() {
    //given
    RegisterProduct.Request request = RegisterProduct.Request.builder()
        .productName("상품명")
        .price(100L)
        .amount(1L)
        .maker("Commerce")
        .build();

    given(productRepository.existsByProductName(anyString())).willReturn(false);
    given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> productService.registerProduct(request, "홍길동"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }
}