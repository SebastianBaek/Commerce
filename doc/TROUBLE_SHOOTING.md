# Trouble Shooting
프로젝트를 진행하면서 발생한 문제점들과 해결법 서술합니다.

## 순환참조 오류
- 로그인 인증시 UserDetails를 구현한 UserService와 TokenProvider이 서로를 멤버변수로 가지고 있어 순환참조 오류가 발생.
- UserDetails를 구현하는 UserDetailsServiceImpl를 따로 선언하여 주입시킴.

## 의존성 오류

- 애플리케이션을 실행했을 때는 발생하지 않았던 오류가 테스트에서 발생.
- JwtAuthenticationFilter에서 tokenProvider를 주입 받지 못하는 오류.
- @WebMvcTest는 테스트의 경량화를 위한 어노테이션으로 스캔 대상이 아닌 @Comonent였던 TokenProvider을 인식하지 못한 것.
- 테스트 패키지에서 ComponentScan할 CommonApiTest를 상속하여 해결.
- UserService의 JavaMailSender을 주입 받지 못하는 오류.
- AppConfig에 JavaMailSender Bean을 등록하여 해결.