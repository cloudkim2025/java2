package com.example.tokenexample.config.filter;

import com.example.tokenexample.jwt.TokenProvider;
import com.example.tokenexample.model.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증을 수행하는 Spring Security 필터 클래스
 * OncePerRequestFilter를 상속하여 요청마다 한 번씩 실행됨
 */
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider; // JWT 관련 로직을 처리하는 TokenProvider
    private final static String HEADER_AUTHORIZATION = "Authorization"; // HTTP 헤더에서 토큰을 찾기 위한 키 값
    private final static String TOKEN_PREFIX = "Bearer "; // 토큰 앞에 붙는 "Bearer " 문자열

    /**
     * 요청이 들어올 때마다 실행되는 메서드로, JWT를 검사하여 인증을 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // HTTP 요청에서 JWT 토큰을 추출
        String token = resolveToken(request);

        if (token != null && tokenProvider.validToken(token) == 1) { // 토큰이 유효한 경우
            // JWT에서 인증 정보를 가져와 SecurityContext에 설정
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 요청 속성(attribute)에 Member 객체 저장 (추후 컨트롤러에서 접근 가능)
            Member member = tokenProvider.getTokenDetails(token);
            request.setAttribute("member", member);
        } else if (token != null && tokenProvider.validToken(token) == 2) { // 토큰이 만료된 경우
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 응답 반환
            return; // 더 이상 필터 체인을 진행하지 않고 종료
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 JWT 토큰을 추출하는 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION); // "Authorization" 헤더에서 값 가져오기

        // Bearer 토큰인지 확인하고, "Bearer " 접두사를 제외한 실제 토큰 반환
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length()); // "Bearer " 이후의 문자열이 실제 토큰 값
        }

        return null; // 유효한 토큰이 없을 경우 null 반환
    }
}
