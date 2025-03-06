package com.example.tokenexample.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 관련 설정 값을 관리하는 클래스.
 * application.yml 또는 application.properties에서 설정된 값을 자동으로 매핑하여 사용함.
 */
@Getter
@Setter
@Component
@ConfigurationProperties("jwt") // "jwt" 프리픽스를 가진 설정 값을 자동으로 매핑
public class JwtProperties {

    /**
     * JWT 발급자(issuer) 정보.
     * 토큰의 발급처를 나타내며, 보안 및 검증에 사용됨.
     */
    private String issuer;

    /**
     * JWT 서명(Signature) 및 검증을 위한 비밀 키(secretKey).
     * Base64 인코딩된 문자열로 저장되며, JWT의 서명을 생성 및 검증하는 데 사용됨.
     */
    private String secretKey;
}
