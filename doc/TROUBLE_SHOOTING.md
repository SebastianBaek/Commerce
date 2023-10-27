# Trouble Shooting
프로젝트를 진행하면서 발생한 문제점들과 해결법 서술합니다.

- 로그인 인증시 UserDetails를 구현한 UserService와 TokenProvider이 서로를 멤버변수로 가지고 있어 순환참조 오류가 발생.
- UserDetails를 구현하는 UserDetailsServiceImpl를 따로 선언하여 주입시킴.