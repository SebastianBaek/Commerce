package com.example.commerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.commerce.CommonApiTest;
import com.example.commerce.model.RegisterUser;
import com.example.commerce.model.RegisterUser.Request;
import com.example.commerce.repository.UserRepository;
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
  private UserRepository userRepository;

  @Test
  @DisplayName("유저 회원가입 API 테스트")
  void registerUserTest() throws Exception {
    //given
    given(userService.registerUser(any()))
        .willReturn(RegisterUser.Response.builder()
            .username("홍길동")
            .createdAt(LocalDateTime.now())
            .build());

    RegisterUser.Request request = Request.builder()
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
}