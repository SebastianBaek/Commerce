package com.example.commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.ModifyProduct;
import com.example.commerce.model.ProductInfo;
import com.example.commerce.model.RegisterProduct;
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

  @Test
  @DisplayName("상품 정보 수정 성공 테스트")
  void modifyProductSuccess() {
    //given
    ModifyProduct.Request request = ModifyProduct.Request.builder()
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

    given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
    given(productRepository.findByIdAndUser(anyLong(), any())).willReturn(Optional.of(product));
    //when
    productService.modifyProduct(request, 1L, user.getUsername());
    //then
    verify(productRepository).save(any());
  }

  @Test
  @DisplayName("상품 정보 수정시 회원 찾기 실패 테스트")
  void modifyProductUserNotFoundFail() {
    //given
    ModifyProduct.Request request = ModifyProduct.Request.builder()
        .productName("상품명")
        .price(100L)
        .amount(1L)
        .maker("Commerce")
        .build();

    given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> productService.modifyProduct(request, 1L, "홍길동"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("상품 정보 수정시 상품 찾기 실패 테스트")
  void modifyProductProductNotFoundFail() {
    //given
    ModifyProduct.Request request = ModifyProduct.Request.builder()
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

    given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
    given(productRepository.findByIdAndUser(anyLong(), any())).willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> productService.modifyProduct(request, 1L, "홍길동"));
    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("판매자의 상품 불러오기 성공 테스트")
  void getAllSellerProductsSuccess() {
    //given
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

    List<Product> products = new ArrayList<>();
    products.add(apple);
    products.add(pear);

    User user = User.builder()
        .email("test@naver.com")
        .username("홍길동")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_SELLER"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.of(user));
    given(productRepository.findAllByUser(any()))
        .willReturn(Optional.of(products));
    //when
    List<ProductInfo> productInfos = productService.getAllSellerProducts("홍길동");
    //then
    assertEquals("사과", productInfos.get(0).getProductName());
    assertEquals("배", productInfos.get(1).getProductName());
  }

  @Test
  @DisplayName("판매자의 상품 불러올 시 회원 찾기 실패 테스트")
  void getAllSellerProductsUserNotFoundFail() {
    //given
    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> productService.getAllSellerProducts("홍길동"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("판매자의 상품 불러올 시 상품 찾기 실패 테스트")
  void getAllSellerProductsProductNotFoundFail() {
    //given
    User user = User.builder()
        .email("test@naver.com")
        .username("홍길동")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_SELLER"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.of(user));
    given(productRepository.findAllByUser(any()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> productService.getAllSellerProducts("홍길동"));
    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("판매자의 상품 삭제 성공 테스트")
  void removeProductSuccess() {
    //given
    User user = User.builder()
        .email("test@naver.com")
        .username("홍길동")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_SELLER"))
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

    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.of(user));
    given(productRepository.findByIdAndUser(anyLong(), any()))
        .willReturn(Optional.of(apple));
    //when
    productService.removeProduct(1L, "홍길동");
    //then
    verify(productRepository).delete(any());
  }

  @Test
  @DisplayName("판매자의 상품 삭제시 회원 찾기 실패 테스트")
  void removeProductUserNotFoundFail() {
    //given
    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> productService.removeProduct(1L, "홍길동"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("판매자의 상품 삭제시 상품 찾기 실패 테스트")
  void removeProductProductNotFoundFail() {
    //given
    User user = User.builder()
        .email("test@naver.com")
        .username("홍길동")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_SELLER"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByUsername(anyString()))
        .willReturn(Optional.of(user));
    given(productRepository.findByIdAndUser(anyLong(), any()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(
        CustomException.class, () -> productService.removeProduct(1L, "홍길동"));
    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, e.getErrorCode());
  }
}