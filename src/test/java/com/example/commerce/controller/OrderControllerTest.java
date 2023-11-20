package com.example.commerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commerce.CommonApiTest;
import com.example.commerce.model.OrderProduct;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.service.CartService;
import com.example.commerce.service.CouponService;
import com.example.commerce.service.OrderService;
import com.example.commerce.service.ProductService;
import com.example.commerce.service.SearchService;
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

@WebMvcTest(OrderController.class)
@MockBean(JpaMetamodelMappingContext.class)
class OrderControllerTest extends CommonApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderService orderService;

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

  @MockBean
  private SearchService searchService;

  @Test
  @DisplayName("회원의 주문 성공 테스트")
  @WithMockUser(authorities = {"ROLE_COMMON"})
  void orderProductSuccess() throws Exception {
    //given
    OrderProduct.Request request = OrderProduct.Request.builder()
        .productId(1L)
        .amount(1L)
        .address("서울시")
        .build();

    OrderProduct.Response response = OrderProduct.Response.builder()
        .orderId(1L)
        .productName("사과")
        .sum(1000L)
        .amount(1L)
        .address("서울시")
        .build();

    given(orderService.orderProduct(any(), anyString()))
        .willReturn(response);
    //when
    //then
    mockMvc.perform(
            post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)))
        .andDo(print());
  }
}