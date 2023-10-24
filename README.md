# 📦🚚 E-Commerce Project

## 소개
간단한 이커머스 서비스 프로젝트

## 프로젝트 기능 및 설계
### 회원가입과 로그인
- 회원가입
    - 회원가입시 이메일, 아이디, 비밀번호, 생년월일, 성별, 주소, 전화번호 정보가 필요
    - 이메일 인증이 완료되면 정상적으로 회원가입을 완료하고, 그렇지 않으면 보류
    - 회원가입시 이미 회원가입된 이메일과 아이디로 회원가입을 시도하면 에러를 발생
    - 일반회원과 판매자회원은 회원가입시에 상품등록 권한 유무에 따라 구분되며, 다른 회원의 정보를 보거나 수정할 수 없음
    - 이메일 전송은 SMTP Gmail 사용


- 로그인
    - 로그인시 회원가입한적 없는 아이디를 이용하여 로그인을 시도하면 에러가 발생
    - 로그인시 비밀번호가 일치하지 않는다면 에러가 발생
    - 회원가입시에 부여된 권한에 따라 JWT을 생성하여 응답
    - 로그인한 사용자는 이메일과 아이디를 제외한 회원정보들을 수정 가능


- 아이디 및 비밀번호 찾기
    - 이메일을 입력받고 해당 이메일로 가입한 회원이 있으면 이메일로 아이디 발송
    - 아이디와 이메일을 입력받고 해당 이메일로 가입한 회원이 있으면 이메일로 임시 비밀번호를 발급


- 관리자 로그인
    - 관리자 로그인 시 상품을 등록, 삭제, 회원, 주문 관리를 할 수 있음 (모든 권한을 가짐)

### 주문 및 조회 기능
- 상품명 검색
    - 상품 검색은 로그인 여부와 관계없이 가능
    - 검색창에 입력한 단어로 시작되는 상품명을 가진 상품들을 자동완성 응답
    - 상품은 낮은가격순, 높은가격순, 판매량순, 별점순, 최신순으로 조회 가능


- 상품 상세 조회
    - 상품 상세 조회 시 상품명, 가격, 리뷰, 현재 재고, 제조사등 상품에 대한 정보를 확인 가능


- 상품 주문
    - 구매할 상품의 수량, 배송받을 주소를 입력해야 주문이 가능
    - 쿠폰 적용은 선택사항

### 상품 장바구니 기능

- 상품 담기
    - 로그인 된 회원은 상품을 본인의 장바구니에 담기 가능


- 장바구니 목록
    - 로그인 된 회원은 본인의 장바구니를 조회 가능


- 장바구니 삭제
    - 로그인 된 회원은 본인의 장바구니 목록을 삭제 가능


- 쿠폰
    - 회원의 모든 쿠폰을 조회
    - 쿠폰은 한개의 상품에 적용 가능, 상품 가격은 0원 이하로 내려갈 수 없음
    - 모든 회원은 회원가입시 5000원 할인쿠폰을 발급

### 상품 리뷰 기능

- 리뷰 작성
    - 주문을 완료한 고객은 30일 이내에 상품 이용 후기, 별점을 등록 가능
    - 리뷰 등록을 완료하면 상품 판매자에게 알림

## ERD
![ERD](doc/img/erd.png)

## Trouble Shooting
[트러블 슈팅 히스토리](doc/TROUBLE_SHOOTING.md)

### Tech Stack
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white">
</div>
