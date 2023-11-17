package com.example.commerce.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commerce.CommonApiTest;
import com.example.commerce.model.CartInfo;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.service.CartService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CartController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CartControllerTest extends CommonApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CartService cartService;

  @MockBean
  private ProductService productService;

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @Test
  @DisplayName("회원의 장바구니 담기 성공 테스트")
  @WithMockUser(authorities = {"ROLE_COMMON"})
  void addProductSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(
            post("/cart/add/1"))
        .andExpect(status().isOk())
        .andDo(print());
    verify(cartService).addProduct(anyLong(), anyString());
  }

  @Test
  @DisplayName("회원의 장바구니 상품 불러오기 성공 테스트")
  @WithMockUser(authorities = {"ROLE_COMMON"})
  void getProductsSuccess() throws Exception {
    //given
    CartInfo cartInfo1 = CartInfo.builder()
        .productName("apple")
        .price(1000L)
        .amount(3L)
        .build();

    CartInfo cartInfo2 = CartInfo.builder()
        .productName("pear")
        .price(2000L)
        .amount(5L)
        .build();

    List<CartInfo> cartInfos = new ArrayList<>();
    cartInfos.add(cartInfo1);
    cartInfos.add(cartInfo2);

    given(cartService.getProducts(anyString()))
        .willReturn(cartInfos);
    //when
    //then
    mockMvc.perform(
            get("/cart/get"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(cartInfos)))
        .andDo(print());
    verify(cartService).getProducts(anyString());
  }
}