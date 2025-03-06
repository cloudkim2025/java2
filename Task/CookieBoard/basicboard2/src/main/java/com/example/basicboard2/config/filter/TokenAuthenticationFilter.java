package com.example.basicboard2.config.filter;

import com.example.basicboard2.config.jwt.TokenProvider;
import com.example.basicboard2.model.Member;
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
 * JWT 기반 인증을 처리하는 Spring Security 필터.
 * - 요청이 들어올 때마다 JWT를 검증하고, 인증 정보를 설정하는 역할을 수행.
 */
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider; // JWT 관련 기능을 제공하는 TokenProvider
    private final static String HEADER_AUTHORIZATION = "Authorization"; // 요청 헤더에서 토큰을 찾기 위한 키 값
    private final static String TOKEN_PREFIX = "Bearer "; // JWT 앞에 붙는 "Bearer " 문자열

    /**
     * HTTP 요청을 가로채어 JWT를 검증하고, 인증 정보를 설정하는 메서드.
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰을 추출
        String token = resolveToken(request);

        if (token != null && tokenProvider.validToken(token) == 1) { // 토큰이 유효한 경우
            // JWT에서 인증 정보를 가져와 Spring Security의 인증 컨텍스트에 설정
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 요청 속성(attribute)에 사용자(Member) 객체 저장 (컨트롤러에서 접근 가능)
            Member member = tokenProvider.getTokenDetails(token);
            request.setAttribute("member", member);
        } else if (token != null && tokenProvider.validToken(token) == 2) { // 토큰이 만료된 경우
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 Unauthorized 응답 반환
            return; // 더 이상 필터 체인을 진행하지 않음
        }

        // 필터 체인을 계속 진행하여 다음 필터 실행
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출하는 메서드.
     * @param request HTTP 요청
     * @return Bearer 토큰 문자열 (접두사 "Bearer " 제외)
     */
    private String resolveToken(HttpServletRequest request) {
        // "Authorization" 헤더에서 토큰 값 가져오기
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);

        // "Bearer " 접두사가 포함된 경우, 접두사 제거 후 실제 JWT 토큰만 반환
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length()); // "Bearer " 이후의 문자열 반환
        }

        return null; // 유효한 토큰이 없으면 null 반환
    }
}
