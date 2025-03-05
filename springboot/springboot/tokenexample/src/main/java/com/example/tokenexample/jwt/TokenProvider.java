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

    private final JwtProperties jwtProperties;

    //JWT 생성
    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date();
        return makeToken(
                member,
                new Date( now.getTime() + expiredAt.toMillis() ) // 만료시간 설정
        );
    }
    //- `expiredAt`만큼 현재 시간(`now`)을 더해 만료 시간을 설정.
    //- `makeToken()`을 호출하여 JWT 생성.


    //  JWT에서 사용자 정보 추출
    public Member getTokenDetails(String token) {
        Claims claims = getClaims(token);

        return Member.builder()
                .id(claims.get("id", Long.class))
                .userId(claims.getSubject())
                .userName(claims.get("userName", String.class))
                .role(Role.valueOf(claims.get("role",String.class)))
                .build();
    }
    //- JWT에서 Claim을 읽어 `Member` 객체를 생성하여 반환.
    //- `role`, `userName`, `id` 등의 정보를 포함.



    //JWT에서 Spring Security Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        //Claims에서 역할을 추출하고 , GrantedAuthority로 변환
        List<GrantedAuthority> authorities =Collections.singletonList(
                new SimpleGrantedAuthority(claims.get("role", String.class))
        );

        // UserDetails 객체 생성
        UserDetails userDetails = new User(claims.getSubject(),"", authorities);

        //UsernamePasswordAuthenticationToken 생성
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);

    }
    //- `getClaims(token)`을 호출하여 JWT 내부의 **사용자 역할(role)을 가져옴**.
    //- Spring Security에서 인가(Authorization)를 처리하기 위해 **`GrantedAuthority`로 변환**.
    //- **Spring Security의 `UserDetails` 객체를 생성**.
    //- `UsernamePasswordAuthenticationToken`을 반환하여 **Spring Security의 인증(Authentication) 객체로 사용 가능**.


    public int validToken(String token) {
        try {
            getClaims(token);

            return 1;
        } catch (ExpiredJwtException e) {
            log.info("Token이 만료되었습니다.");
            return 2;
        }catch (Exception e) {
            //복호화 과정에서 에러가 나면 유효하지 않은 토큰
            System.out.println("Token  복호화 에러 :" +e.getMessage());
            return 3;
        }
    }


    // JWT 내부 생성 로직
    private String makeToken(Member member, Date expired) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expired)
                .setSubject(member.getUserId())
                .claim("id", member.getId())
                .claim("role", member.getRole().name())
                .claim("userName", member.getUserName())
                .compact();
    }
    //- **JWT Payload에 `role`, `userName`, `id`를 추가하여 사용자 정보를 포함**.
    //- 서명(Signature)을 포함하여 JWT를 최종 생성.



    //JWT 서명 검증 및 Payload 파싱
    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    //- JWT를 파싱하여 **Payload(Claims)를 반환**.
    //- **서명(Signature) 검증을 수행**하여 유효한 토큰인지 확인.


        //비밀 키 생성
    private SecretKey getSecretKey(){
         byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //- `jwtProperties.getSecretKey()`를 가져와 **Base64 디코딩 후 SecretKey 생성**.
    //- `Keys.hmacShaKeyFor()`를 사용하여 **HMAC SHA 기반의 서명 키 생성**.
}
