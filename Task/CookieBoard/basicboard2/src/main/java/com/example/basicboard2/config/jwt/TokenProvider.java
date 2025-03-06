package com.example.basicboard2.config.jwt;

import com.example.basicboard2.model.Member;
import com.example.basicboard2.type.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * JWT 토큰을 생성하고 검증하는 서비스 클래스.
 * - JWT 발급, 정보 추출, 인증 객체 변환, 유효성 검사 등의 기능을 수행.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties; // JWT 설정 정보를 주입받음

    /**
     * JWT 토큰을 생성하는 메서드.
     * @param member JWT에 포함될 사용자 정보
     * @param expiredAt 토큰 만료 시간
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date(); // 현재 시간
        return makeToken(
                member,
                new Date(now.getTime() + expiredAt.toMillis()) // 만료 시간 설정
        );
    }

    /**
     * JWT에서 사용자 정보를 추출하는 메서드.
     * @param token JWT 토큰
     * @return Member 객체 (사용자 정보)
     */
    public Member getTokenDetails(String token) {
        Claims claims = getClaims(token); // JWT에서 Claims(페이로드) 추출

        return Member.builder()
                .id(claims.get("id", Long.class)) // 사용자 ID
                .userId(claims.getSubject()) // 사용자 로그인 ID
                .userName(claims.get("userName", String.class)) // 사용자 이름
                .role(Role.valueOf(claims.get("role", String.class))) // 역할(Role)
                .build();
    }

    /**
     * JWT에서 Spring Security Authentication 객체를 생성하는 메서드.
     * @param token JWT 토큰
     * @return Spring Security의 Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // JWT에서 Claims 추출

        // Claims에서 역할(Role) 정보를 가져와 GrantedAuthority로 변환
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(claims.get("role", String.class))
        );

        // UserDetails 객체 생성
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

        // UsernamePasswordAuthenticationToken 객체 생성 후 반환
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    /**
     * JWT 토큰의 유효성을 검사하는 메서드.
     * @param token 검증할 JWT 토큰
     * @return 1: 유효한 토큰, 2: 만료된 토큰, 3: 유효하지 않은 토큰
     */
    public int validToken(String token) {
        try {
            getClaims(token); // 토큰을 파싱하여 검증 수행
            return 1; // 유효한 경우 1 반환
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            log.info("Token이 만료되었습니다.");
            return 2;
        } catch (Exception e) {
            // 복호화 과정에서 에러가 발생한 경우 유효하지 않은 토큰으로 간주
            log.error("Token 복호화 에러 : " + e.getMessage());
            return 3;
        }
    }

    /**
     * JWT를 생성하는 내부 메서드.
     * @param member JWT에 포함할 사용자 정보
     * @param expired JWT 만료 시간
     * @return 생성된 JWT 문자열
     */
    private String makeToken(Member member, Date expired) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 설정
                .setIssuer(jwtProperties.getIssuer()) // 발급자(iss) 설정
                .setIssuedAt(now) // 발급 시간 설정
                .setExpiration(expired) // 만료 시간 설정
                .setSubject(member.getUserId()) // subject에 userId 설정
                .claim("id", member.getId()) // 사용자 ID 포함
                .claim("role", member.getRole().name()) // 역할(Role) 포함
                .claim("userName", member.getUserName()) // 사용자 이름 포함
                .compact(); // 최종 JWT 생성
    }

    /**
     * JWT의 서명을 검증하고 Payload(Claims)를 반환하는 메서드.
     * @param token 검증할 JWT 토큰
     * @return JWT의 Payload(Claims)
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) // 서명 검증을 위한 SecretKey 설정
                .build()
                .parseClaimsJws(token) // 토큰을 파싱하여 검증 수행
                .getBody(); // Payload(Claims) 반환
    }

    /**
     * JWT 서명을 위한 SecretKey를 생성하는 메서드.
     * @return 생성된 SecretKey 객체
     */
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecretKey()); // Base64 디코딩
        return Keys.hmacShaKeyFor(keyBytes); // HMAC SHA 기반 서명 키 생성
    }
}
