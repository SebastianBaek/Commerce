package com.example.commerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commerce.CommonApiTest;
import com.example.commerce.model.RegisterProduct;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.service.ProductService;
import com.example.commerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
}