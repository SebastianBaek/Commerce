package com.example.commerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commerce.CommonApiTest;
import com.example.commerce.model.ModifyProduct;
import com.example.commerce.model.ProductInfo;
import com.example.commerce.model.RegisterProduct;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.service.CartService;
import com.example.commerce.service.CouponService;
import com.example.commerce.service.ProductService;
import com.example.commerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductControllerTest extends CommonApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProductService productService;

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private CartService cartService;

  @MockBean
  private CouponService couponService;

  @Test
  @DisplayName("판매자의 상품 등록 성공 테스트")
  @WithMockUser(authorities = {"ROLE_SELLER"})
  void registerProductSuccess() throws Exception {
    //given
    given(productService.registerProduct(any(), anyString()))
        .willReturn(RegisterProduct.Response.builder()
            .productId(1L)
            .productName("상품명")
            .build());

    RegisterProduct.Request request = RegisterProduct.Request.builder()
        .productName("상품명")
        .price(100L)
        .amount(1L)
        .maker("Commerce")
        .build();
    //when
    //then
    mockMvc.perform(
            post("/product/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").exists())
        .andExpect(jsonPath("$.productName").exists())
        .andDo(print());
    verify(productService).registerProduct(any(), anyString());
  }

  @Test
  @DisplayName("상품 등록 권한 실패 테스트")
  void registerProductFail() throws Exception {
    //given
    RegisterProduct.Request request = RegisterProduct.Request.builder()
        .productName("상품명")
        .price(100L)
        .amount(1L)
        .maker("Commerce")
        .build();
    //when
    //then
    mockMvc.perform(
            post("/product/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("판매자의 상품 수정 성공 테스트")
  @WithMockUser(authorities = {"ROLE_SELLER"})
  void modifyProductSuccess() throws Exception {
    //given
    ModifyProduct.Request request = ModifyProduct.Request.builder()
        .productName("상품명")
        .price(100L)
        .amount(1L)
        .maker("Commerce")
        .build();
    //when
    //then
    mockMvc.perform(
            put("/product/modify/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());
    verify(productService).modifyProduct(any(), anyLong(), anyString());
  }

  @Test
  @DisplayName("판매자의 상품 불러오기 성공 테스트")
  @WithMockUser(authorities = {"ROLE_SELLER"})
  void getAllSellerProductsSuccess() throws Exception {
    //given
    ProductInfo apple = ProductInfo.builder()
        .productName("사과")
        .price(1000L)
        .amount(100L)
        .maker("의성")
        .rating(null)
        .sales(0L)
        .build();
    ProductInfo pear = ProductInfo.builder()
        .productName("배")
        .price(1000L)
        .amount(100L)
        .maker("나주")
        .rating(null)
        .sales(0L)
        .build();

    List<ProductInfo> productInfos = new ArrayList<>();
    productInfos.add(apple);
    productInfos.add(pear);

    given(productService.getAllSellerProducts(anyString()))
        .willReturn(productInfos);
    //when
    //then
    mockMvc.perform(
            get("/product/getAll")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(productInfos)))
        .andDo(print());
    verify(productService).getAllSellerProducts(anyString());
  }

  @Test
  @DisplayName("판매자의 상품 삭제 성공 테스트")
  @WithMockUser(authorities = {"ROLE_SELLER"})
  void removeProductSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(
            delete("/product/remove/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
    verify(productService).removeProduct(anyLong(), anyString());
  }
}