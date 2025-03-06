package com.example.basicboard2.config;

import com.example.basicboard2.config.jwt.TokenProvider;
import com.example.basicboard2.model.Member;
import com.example.basicboard2.type.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * TokenProvider 클래스의 JWT 생성 기능을 테스트하는 클래스.
 */
@SpringBootTest // Spring Boot의 테스트 환경을 로드하여 실행
class TokenProviderTest {

    @Autowired
    TokenProvider tokenProvider; // TokenProvider 주입

    /**
     * JWT 토큰 생성 기능을 테스트하는 메서드.
     * - Member 객체를 생성하여 JWT를 발급하고, 정상적으로 생성되었는지 검증.
     */
    @Test
    void 토큰생성_테스트() {
        // given (테스트 데이터 준비)
        Member member = Member.builder()
                .id(0L) // 테스트용 사용자 ID
                .userId("test") // 테스트용 로그인 ID
                .password("123456") // 테스트용 비밀번호 (실제 환경에서는 보안상 암호화해야 함)
                .userName("test") // 테스트용 사용자 이름
                .role(Role.ROLE_USER) // 테스트용 사용자 역할 설정
                .build();

        Duration duration = Duration.ofHours(1); // JWT 만료 시간: 1시간

        // when (테스트 실행: JWT 생성)
        String token = tokenProvider.generateToken(member, duration);

        // then (검증)
        System.out.println(token); // 생성된 토큰 출력
        assertNotNull(token); // 토큰이 정상적으로 생성되었는지 검증
    }
}
