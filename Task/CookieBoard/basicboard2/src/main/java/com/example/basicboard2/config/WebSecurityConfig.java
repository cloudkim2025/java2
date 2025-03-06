package com.example.basicboard2.config;

import com.example.basicboard2.config.filter.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security 설정 클래스.
 * - JWT 기반의 인증을 적용하고, 특정 요청을 허용 또는 차단.
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter; // JWT 인증 필터

    /**
     * 정적 리소스(Security Filter에서 제외할 경로)를 설정하는 메서드.
     * - "/static/**", "/css/**", "/js/**" 등 정적 파일 요청은 보안 검사 없이 허용.
     *
     * @return WebSecurityCustomizer 객체
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/static/**",
                        "/css/**",
                        "/js/**"
                ); // 정적 리소스 경로 무시
    }

    /**
     * HTTP 보안 설정을 구성하는 메서드.
     * - CSRF 비활성화
     * - JWT를 사용한 인증 처리
     * - 특정 경로 접근 허용 및 인증 요구 설정
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 예외 발생 가능
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (JWT 사용 시 필요 없음)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) // 세션을 사용하지 않는 Stateless 인증 방식 적용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/", "GET"),
                                new AntPathRequestMatcher("/member/join", "GET"),
                                new AntPathRequestMatcher("/member/login", "GET"),
                                new AntPathRequestMatcher("/join", "POST"),
                                new AntPathRequestMatcher("/login", "POST"),
                                new AntPathRequestMatcher("/logout", "POST")
                        ).permitAll() // 특정 요청 경로는 인증 없이 접근 가능
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 기능 비활성화 (JWT 기반으로 관리)
                // JWT 필터 추가 (UsernamePasswordAuthenticationFilter 실행 전에 실행)
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Spring Security의 인증 관리자(AuthenticationManager) 설정.
     * @param configuration AuthenticationConfiguration 객체
     * @return AuthenticationManager 객체
     * @throws Exception 예외 발생 가능
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 비밀번호를 암호화하기 위한 BCryptPasswordEncoder Bean 생성.
     * @return BCryptPasswordEncoder 객체
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
