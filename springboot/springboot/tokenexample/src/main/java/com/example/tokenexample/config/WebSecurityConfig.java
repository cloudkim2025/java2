package com.example.tokenexample.config;

import com.example.tokenexample.config.filter.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정을 담당하는 클래스.
 * JWT 기반 인증 방식을 사용하며, 세션을 사용하지 않는 Stateless 방식으로 구성됨.
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter; // JWT 인증 필터

    /**
     * Spring Security의 보안 필터 체인을 설정하는 메서드.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF(Cross Site Request Forgery) 보호 기능 비활성화 (JWT 기반 인증이므로 필요 없음)
                .csrf(AbstractHttpConfigurer::disable)

                // 세션을 사용하지 않도록 Stateless 방식 설정 (JWT 기반 인증)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 로그아웃 기능 비활성화 (JWT 사용 시 불필요)
                .logout(AbstractHttpConfigurer::disable)

                // JWT 필터를 Spring Security 필터 체인 앞에 추가
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // 설정된 SecurityFilterChain 객체를 반환
    }

    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록.
     * Spring Security에서 사용자 비밀번호를 저장할 때 사용됨.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
