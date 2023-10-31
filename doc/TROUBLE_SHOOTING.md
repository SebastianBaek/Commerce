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

## 데이터 매핑 오류

- 서버에서 요청을 받을 때 @RequestBody를 선언한 클래스에 매핑을 하면서 발생.
- 예외는 HttpMessageNotReadableException 였고 두개 이상의 필드를 가진 클래스에 매핑을 하는 경우엔 발생하지 않음.
- 디폴트 생성자가 없어서 역직렬화를 못해 생긴 오류.
-
참고 [[Spring] @RequestBody에 기본생성자만 필요하고 Setter는 필요없는 이유 - 1.url](..%2F..%2F..%2F..%2FUsers%2Fhing%2FAppData%2FLocal%2FTemp%2F%5BSpring%5D%20%40RequestBody%EC%97%90%20%EA%B8%B0%EB%B3%B8%EC%83%9D%EC%84%B1%EC%9E%90%EB%A7%8C%20%ED%95%84%EC%9A%94%ED%95%98%EA%B3%A0%20Setter%EB%8A%94%20%ED%95%84%EC%9A%94%EC%97%86%EB%8A%94%20%EC%9D%B4%EC%9C%A0%20-%201.url)