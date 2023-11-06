package com.example.commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.commerce.common.MailComponent;
import com.example.commerce.domain.User;
import com.example.commerce.exception.CustomException;
import com.example.commerce.exception.ErrorCode;
import com.example.commerce.model.FindPasswordRequest;
import com.example.commerce.model.FindUsernameRequest;
import com.example.commerce.model.LoginUser;
import com.example.commerce.model.RegisterUser;
import com.example.commerce.repository.UserRepository;
import com.example.commerce.security.TokenProvider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private UserRepository userRepository;
  @Mock
  private TokenProvider tokenProvider;
  @Mock
  private MailComponent mailComponent;
  @InjectMocks
  private UserService userService;

  @Test
  @DisplayName("유저 회원가입 성공 테스트")
  void registerUserSuccess() {
    //given
    RegisterUser.Request request = RegisterUser.Request.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .birth(LocalDate.now().minusDays(1))
        .address("서울")
        .phone("01012345678")
        .role(Collections.singletonList("ROLE_COMMON"))
        .build();

    User user = User.builder()
        .email(request.getEmail())
        .username(request.getUsername())
        .password(request.getPassword())
        .emailVerification(false)
        .roles(request.getRole())
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.save(any())).willReturn(user);
    given(userRepository.existsByEmail(any())).willReturn(false);
    given(userRepository.existsByUsername(any())).willReturn(false);
    //when
    RegisterUser.Response response = userService.registerUser(request);
    //then
    assertEquals("test", response.getUsername());
    assertNotNull(response.getCreatedAt());
  }

  @Test
  @DisplayName("유저 회원가입 이메일 중복 실패 테스트")
  void registerUserEmailDuplicatedFail() {
    //given
    RegisterUser.Request request = RegisterUser.Request.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .birth(LocalDate.now().minusDays(1))
        .address("서울")
        .phone("01012345678")
        .role(Collections.singletonList("ROLE_COMMON"))
        .build();

    given(userRepository.existsByEmail(any())).willReturn(true);
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userService.registerUser(request));
    //then
    assertEquals(ErrorCode.ALREADY_EXISTS_USER_EMAIL, e.getErrorCode());
  }

  @Test
  @DisplayName("유저 회원가입 아이디 중복 실패 테스트")
  void registerUsernameDuplicatedFail() {
    //given
    RegisterUser.Request request = RegisterUser.Request.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .birth(LocalDate.now().minusDays(1))
        .address("서울")
        .phone("01012345678")
        .role(Collections.singletonList("ROLE_COMMON"))
        .build();

    given(userRepository.existsByEmail(any())).willReturn(false);
    given(userRepository.existsByUsername(any())).willReturn(true);
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userService.registerUser(request));
    //then
    assertEquals(ErrorCode.ALREADY_EXISTS_USERNAME, e.getErrorCode());
  }

  @Test
  @DisplayName("유저 로그인 성공 테스트")
  void loginUserSuccess() {
    //given
    LoginUser.Request request = LoginUser.Request.builder()
        .username("test")
        .password("1234")
        .build();

    User user = User.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_COMMON"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByUsername(any())).willReturn(Optional.of(user));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    given(tokenProvider.generateToken(anyString(), anyList())).willReturn("token");
    //when
    LoginUser.Response response = userService.loginUser(request);
    //then
    assertEquals("test", response.getUsername());
    assertEquals("token", response.getToken());
  }

  @Test
  @DisplayName("요청한 아이디로 가입한 유저가 없는 로그인 실패 테스트")
  void loginUserNotFoundFail() {
    //given
    LoginUser.Request request = LoginUser.Request.builder()
        .username("test")
        .password("1234")
        .build();

    given(userRepository.findByUsername(any())).willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class, () -> userService.loginUser(request));
    //then
    assertEquals(ErrorCode.USERNAME_AND_PASSWORD_NOT_MATCH, e.getErrorCode());
  }

  @Test
  @DisplayName("요청한 아이디와 비밀번호 불일치 로그인 실패 테스트")
  void loginUsernamePasswordUnMatchFail() {
    //given
    LoginUser.Request request = LoginUser.Request.builder()
        .username("test")
        .password("1234")
        .build();

    User user = User.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .emailVerification(true)
        .roles(Collections.singletonList("ROLE_COMMON"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByUsername(any())).willReturn(Optional.of(user));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
    //when
    CustomException e = assertThrows(CustomException.class, () -> userService.loginUser(request));
    //then
    assertEquals(ErrorCode.USERNAME_AND_PASSWORD_NOT_MATCH, e.getErrorCode());
  }

  @Test
  @DisplayName("이메일 미인증 로그인 실패 테스트")
  void loginUserEmailIsNotVerifiedFail() {
    //given
    LoginUser.Request request = LoginUser.Request.builder()
        .username("test")
        .password("1234")
        .build();

    User user = User.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .emailVerification(false)
        .roles(Collections.singletonList("ROLE_COMMON"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByUsername(any())).willReturn(Optional.of(user));
    given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
    //when
    CustomException e = assertThrows(CustomException.class, () -> userService.loginUser(request));
    //then
    assertEquals(ErrorCode.EMAIL_IS_NOT_VERIFIED, e.getErrorCode());
  }

  @Test
  @DisplayName("이메일 인증 성공 테스트")
  void verifyUserEmailSuccess() {
    //given
    User user = User.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .emailVerification(false)
        .roles(Collections.singletonList("ROLE_COMMON"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
    //when
    userService.verifyUserEmail(1L);
    //then
    verify(userRepository).save(user);
  }

  @Test
  @DisplayName("이메일 인증 실패 테스트")
  void verifyUserEmailFail() {
    //given
    given(userRepository.findById(anyLong())).willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class, () -> userService.verifyUserEmail(1L));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("아이디 찾기 성공 테스트")
  void findUsernameSuccess() {
    //given
    FindUsernameRequest request = new FindUsernameRequest("test@naver.com");

    User user = User.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .emailVerification(false)
        .roles(Collections.singletonList("ROLE_COMMON"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    //when
    userService.findUsername(request);
    //then
    verify(mailComponent).sendUsername(anyString(), anyString());
  }

  @Test
  @DisplayName("아이디 찾기 실패 테스트")
  void findUsernameFail() {
    //given
    FindUsernameRequest request = new FindUsernameRequest("test@naver.com");

    given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userService.findUsername(request));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("임시 비밀번호 발급 성공 테스트")
  void findPasswordSuccess() {
    //given
    FindPasswordRequest request = new FindPasswordRequest("test@naver.com", "test");

    User user = User.builder()
        .email("test@naver.com")
        .username("test")
        .password("1234")
        .emailVerification(false)
        .roles(Collections.singletonList("ROLE_COMMON"))
        .build();
    user.setId(1L);
    user.setCreatedAt(LocalDateTime.now());

    given(userRepository.findByEmailAndUsername(anyString(), anyString()))
        .willReturn(Optional.of(user));
    //when
    userService.findPassword(request);
    //then
    verify(userRepository).save(user);
    verify(mailComponent).sendTemporaryPassword(anyString(), anyString(), anyString());
  }

  @Test
  @DisplayName("임시 비밀번호 발급 실패 테스트")
  void findPasswordFail() {
    //given
    FindPasswordRequest request = new FindPasswordRequest("test@naver.com", "test");

    given(userRepository.findByEmailAndUsername(anyString(), anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userService.findPassword(request));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }
}