## "BasicBoard2 - Spring Boot JWT 인증 및 게시판 시스템"

BasicBoard2는 Spring Boot 기반의 간단한 게시판 프로젝트로, 회원 가입, 로그인, 게시글 CRUD, JWT 인증 및 보안 기능을 포함합니다.

## 기술 스택

- **Backend**: Java 21, Spring Boot 3.4.3, Spring Security, MyBatis, JWT
- **Database**: MySQL (MariaDB 호환)
- **Frontend**: HTML, CSS, JavaScript

## 회원가입 및 로그인 과정

### 1. 회원가입 (Sign-Up)

회원가입 시 사용자 정보를 데이터베이스에 저장합니다.
**POST** `/join`
```json
{
  "userId": "user1",
  "password": "password123",
  "userName": "사용자 이름"
}
```

### 2. 로그인 (Sign-In) 및 JWT 발급
로그인 요청 시 `TokenProvider`가 JWT를 생성하여 반환합니다.

**POST** `/login`
```json
{
  "username": "user1",
  "password": "password123"
}
```
**응답:**
```json
{
  "isLoggined": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI...",
  "userId": "user1",
  "userName": "사용자 이름"
}
```

### 3. JWT 인증 처리 (TokenAuthenticationFilter)
모든 요청에서 `Authorization` 헤더를 확인하여 JWT를 검증합니다.
```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    String token = resolveToken(request);
    if (token != null && tokenProvider.validToken(token) == 1) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
}
```

### 4. 토큰 갱신 (Refresh Token)
액세스 토큰이 만료되면, 클라이언트는 `refreshToken`을 이용해 새로운 액세스 토큰을 요청합니다.

**POST** `/refresh-token`
```http
Cookie: refreshToken=eyJhbGciOiJIUzI1NiJ9...
```
**응답:**
```json
{
  "token": "새로운 액세스 토큰"
}
```

### 5. 로그아웃 (Logout)
로그아웃 시 클라이언트의 쿠키에서 `refreshToken`을 삭제합니다.
```java
CookieUtil.deleteCookie(request, response, "refreshToken");
```

### 6. 사용자 정보 조회
현재 로그인된 사용자의 정보를 반환합니다.

**GET** `/user/info`
**응답:**
```json
{
  "id": 1,
  "userId": "user1",
  "userName": "사용자 이름",
  "role": "ROLE_USER"
}
```
![image](https://github.com/user-attachments/assets/d21e4332-8a72-4b44-89c4-36c0ccfd0a8a)
![image](https://github.com/user-attachments/assets/0a40440c-98c9-4cb9-a303-416c8177a78c)







