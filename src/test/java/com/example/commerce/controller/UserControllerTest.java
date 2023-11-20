package com.example.commerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commerce.CommonApiTest;
import com.example.commerce.model.FindPasswordRequest;
import com.example.commerce.model.FindUsernameRequest;
import com.example.commerce.model.LoginUser;
import com.example.commerce.model.RegisterUser;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.service.CartService;
import com.example.commerce.service.CouponService;
import com.example.commerce.service.OrderService;
import com.example.commerce.service.ProductService;
import com.example.commerce.service.SearchService;
import com.example.commerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest extends CommonApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @MockBean
  private ProductService productService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private CartService cartService;

  @MockBean
  private CouponService couponService;

  @MockBean
  private SearchService searchService;

  @MockBean
  private OrderService orderService;

  @Test
  @DisplayName("유저 회원가입 API 성공 테스트")
  void registerUserSuccess() throws Exception {
    //given
    given(userService.registerUser(any()))
        .willReturn(RegisterUser.Response.builder()
            .username("홍길동")
            .createdAt(LocalDateTime.now())
            .build());

    RegisterUser.Request request = RegisterUser.Request.builder()
        .email("gnsrudqor@naver.com")
        .username("test")
        .password("1234")
        .birth(LocalDate.now().minusDays(1))
        .address("서울")
        .phone("01012345678")
        .build();
    //when
    //then
    mockMvc.perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").exists())
        .andExpect(jsonPath("$.createdAt").exists())
        .andDo(print());

    verify(userService).registerUser(any());
  }

  @Test
  @DisplayName("유저 회원가입 API 유효성 검증 실패 테스트")
  void registerUserInvalidFail() throws Exception {
    //given
    given(userService.registerUser(any()))
        .willReturn(RegisterUser.Response.builder()
            .username("홍길동")
            .createdAt(LocalDateTime.now())
            .build());

    RegisterUser.Request request = RegisterUser.Request.builder()
        .email("gnsrudqor@naver.com")
        .username("")
        .password("1234")
        .birth(LocalDate.now().minusDays(1))
        .address("서울")
        .phone("01012345678")
        .build();
    //when
    //then
    mockMvc.perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.statusCode").exists())
        .andExpect(jsonPath("$.message").exists())
        .andDo(print());
  }

  @Test
  @DisplayName("유저 로그인 API 성공 테스트")
  void loginUserSuccess() throws Exception {
    //given
    given(userService.loginUser(any()))
        .willReturn(LoginUser.Response.builder()
            .username("홍길동")
            .token("ABC")
            .build());

    LoginUser.Request request = LoginUser.Request.builder()
        .username("홍길동")
        .password("1234")
        .build();
    //when
    //then
    mockMvc.perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").exists())
        .andExpect(jsonPath("$.token").exists())
        .andDo(print());

    verify(userService).loginUser(any());
  }

  @Test
  @DisplayName("회원 이메일 인증 성공 테스트")
  void verifyUserEmailSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(
            get("/user/verify/1"))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).verifyUserEmail(anyLong());
  }

  @Test
  @DisplayName("회원 아이디 찾기 성공 테스트")
  void findUsernameSuccess() throws Exception {
    //given
    FindUsernameRequest request = new FindUsernameRequest("test@naver.com");
    //when
    //then
    mockMvc.perform(
            post("/user/find/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());
    verify(userService).findUsername(request);
  }

  @Test
  @DisplayName("회원 임시 비밀번호 발급 성공 테스트")
  void findUsernamePassword() throws Exception {
    //given
    FindPasswordRequest request = new FindPasswordRequest("test@naver.com", "test");
    //when
    //then
    mockMvc.perform(
            patch("/user/find/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());
    verify(userService).findPassword(request);
  }
}