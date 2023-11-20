package com.example.commerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commerce.CommonApiTest;
import com.example.commerce.model.ProductInfo;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.service.CartService;
import com.example.commerce.service.CouponService;
import com.example.commerce.service.OrderService;
import com.example.commerce.service.ProductService;
import com.example.commerce.service.SearchService;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SearchController.class)
@MockBean(JpaMetamodelMappingContext.class)
class SearchControllerTest extends CommonApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private SearchService searchService;

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private CartService cartService;

  @MockBean
  private ProductService productService;

  @MockBean
  private CouponService couponService;

  @MockBean
  private OrderService orderService;

  @Test
  @DisplayName("상품 불러오기 성공 테스트")
  void searchProductSuccess() throws Exception {
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

    given(searchService.searchProduct(any()))
        .willReturn(productInfos);
    //when
    //then
    mockMvc.perform(
            get("/search"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(productInfos)))
        .andDo(print());
    verify(searchService).searchProduct(any());
  }

  @Test
  @DisplayName("상품 상세 정보 불러오기 성공 테스트")
  void getProductInfoSuccess() throws Exception {
    //given
    ProductInfo apple = ProductInfo.builder()
        .productName("사과")
        .price(1000L)
        .amount(100L)
        .maker("의성")
        .rating(null)
        .sales(0L)
        .build();

    given(searchService.getProductInfo(anyLong()))
        .willReturn(apple);
    //when
    //then
    mockMvc.perform(
            get("/search/1"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(apple)))
        .andDo(print());
    verify(searchService).getProductInfo(anyLong());
  }

}