package com.example.tokenexample.jwt;

import com.example.tokenexample.model.Member;
import com.example.tokenexample.type.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties; // JWT 설정 정보를 담고 있는 클래스

    // JWT 생성 메서드
    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date(); // 현재 시간 가져오기
        return makeToken(
                member,
                new Date( now.getTime() + expiredAt.toMillis() ) // 만료시간을 현재 시간에서 expiredAt을 더한 값으로 설정
        );
    }
    //- `expiredAt`만큼 현재 시간(`now`)을 더해 만료 시간을 설정.
    //- `makeToken()`을 호출하여 JWT 생성.

    // JWT에서 사용자 정보 추출 (Member 객체 반환)
    public Member getTokenDetails(String token) {
        Claims claims = getClaims(token); // 토큰에서 Claims(페이로드) 추출

        return Member.builder()
                .id(claims.get("id", Long.class)) // 사용자 ID 추출
                .userId(claims.getSubject()) // subject 값으로 userId 저장
                .userName(claims.get("userName", String.class)) // 사용자 이름 추출
                .role(Role.valueOf(claims.get("role", String.class))) // 사용자 역할(Role) 추출
                .build();
    }
    //- JWT에서 Claim을 읽어 `Member` 객체를 생성하여 반환.
    //- `role`, `userName`, `id` 등의 정보를 포함.

    // JWT에서 Spring Security Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // 토큰에서 Claims(페이로드) 추출

        // Claims에서 역할을 추출하고, GrantedAuthority로 변환
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(claims.get("role", String.class)) // role 정보를 GrantedAuthority 형태로 변환
        );

        // UserDetails 객체 생성 (비밀번호는 빈 문자열)
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

        // UsernamePasswordAuthenticationToken 생성하여 반환
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }
    //- `getClaims(token)`을 호출하여 JWT 내부의 **사용자 역할(role)을 가져옴**.
    //- Spring Security에서 인가(Authorization)를 처리하기 위해 **`GrantedAuthority`로 변환**.
    //- **Spring Security의 `UserDetails` 객체를 생성**.
    //- `UsernamePasswordAuthenticationToken`을 반환하여 **Spring Security의 인증(Authentication) 객체로 사용 가능**.

    // JWT 유효성 검사
    public int validToken(String token) {
        try {
            getClaims(token); // 토큰을 파싱하여 검증 수행
            return 1; // 유효한 경우 1 반환
        } catch (ExpiredJwtException e) {
            log.info("Token이 만료되었습니다."); // 만료된 경우 로그 출력
            return 2; // 만료된 경우 2 반환
        } catch (Exception e) {
            // 복호화 과정에서 에러 발생 시 유효하지 않은 토큰으로 간주
            System.out.println("Token 복호화 에러 :" + e.getMessage());
            return 3; // 오류 발생 시 3 반환
        }
    }
    //- `getClaims(token)`을 호출하여 JWT를 검증.
    //- 만료된 경우 2 반환, 복호화 실패 및 기타 오류 발생 시 3 반환.

    // JWT 내부 생성 로직
    private String makeToken(Member member, Date expired) {
        Date now = new Date(); // 현재 시간

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더에 JWT 타입 설정
                .setIssuer(jwtProperties.getIssuer()) // 발급자(issuer) 설정
                .setIssuedAt(now) // 발급 시간 설정
                .setExpiration(expired) // 만료 시간 설정
                .setSubject(member.getUserId()) // subject에 userId 설정
                .claim("id", member.getId()) // 사용자 ID 추가
                .claim("role", member.getRole().name()) // 역할(Role) 추가
                .claim("userName", member.getUserName()) // 사용자 이름 추가
                .compact(); // 최종 JWT 생성
    }
    //- **JWT Payload에 `role`, `userName`, `id`를 추가하여 사용자 정보를 포함**.
    //- 서명(Signature)을 포함하여 JWT를 최종 생성.

    // JWT 서명 검증 및 Payload 파싱
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) // 서명 검증을 위한 키 설정
                .build()
                .parseClaimsJws(token) // 토큰을 파싱하여 유효성 검사 수행
                .getBody(); // Payload(Claims) 반환
    }
    //- JWT를 파싱하여 **Payload(Claims)를 반환**.
    //- **서명(Signature) 검증을 수행**하여 유효한 토큰인지 확인.

    // 비밀 키 생성
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecretKey()); // Base64로 디코딩
        return Keys.hmacShaKeyFor(keyBytes); // HMAC SHA 기반 서명 키 생성
    }
    //- `jwtProperties.getSecretKey()`를 가져와 **Base64 디코딩 후 SecretKey 생성**.
    //- `Keys.hmacShaKeyFor()`를 사용하여 **HMAC SHA 기반의 서명 키 생성**.
}
