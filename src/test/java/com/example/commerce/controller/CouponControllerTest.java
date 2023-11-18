package com.example.commerce.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commerce.CommonApiTest;
import com.example.commerce.model.CouponInfo;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.service.CartService;
import com.example.commerce.service.CouponService;
import com.example.commerce.service.ProductService;
import com.example.commerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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

@WebMvcTest(CouponController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CouponControllerTest extends CommonApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CouponService couponService;

  @MockBean
  private CartService cartService;

  @MockBean
  private ProductService productService;

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @Test
  @DisplayName("회원의 쿠폰 불러오기 성공 테스트")
  @WithMockUser(authorities = {"ROLE_COMMON"})
  void getAllCouponSuccess() throws Exception {
    //given
    CouponInfo coupon1 = CouponInfo.builder()
        .rate(10)
        .expiration(LocalDate.now().plusDays(30))
        .build();
    CouponInfo coupon2 = CouponInfo.builder()
        .saving(5000)
        .expiration(LocalDate.now().plusDays(30))
        .build();

    List<CouponInfo> couponInfos = new ArrayList<>();
    couponInfos.add(coupon1);
    couponInfos.add(coupon2);

    given(couponService.getAllCoupon(anyString()))
        .willReturn(couponInfos);
    //when
    //then
    mockMvc.perform(
            get("/coupon/getAll"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(couponInfos)))
        .andDo(print());
    verify(couponService).getAllCoupon(anyString());
  }

}