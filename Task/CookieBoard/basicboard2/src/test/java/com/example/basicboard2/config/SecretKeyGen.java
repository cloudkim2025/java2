package com.example.basicboard2.config;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * HMAC-SHA512 기반의 비밀키 생성 테스트 클래스
 */
class SecretKeyGen {

    @Test
    void HS512_생성() { // HMAC-SHA512 방식으로 비밀키 생성 테스트
        Mac sha512_HMAC = null;
        String data = "Spring boot basic board 2"; // HMAC 처리할 데이터
        String secretKey = "256-bit-secret"; // 비밀키 (보안을 위해 적절한 키를 설정해야 함)

        try {
            // HmacSHA512 알고리즘을 사용하여 MAC 인스턴스 생성
            sha512_HMAC = Mac.getInstance("HmacSHA512");

            // UTF-8 인코딩을 사용하여 SecretKeySpec 생성
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA512");

            // 생성된 키를 사용하여 HMAC 초기화
            sha512_HMAC.init(keySpec);

            // 데이터(data)를 HMAC으로 변환하여 서명 생성
            byte[] macData = sha512_HMAC.doFinal(data.getBytes("UTF-8"));

            // 생성된 바이트 배열을 Base64 인코딩하여 문자열로 변환
            String secret_key = Base64.getEncoder().encodeToString(macData);

            // 생성된 비밀키 출력
            System.out.println(secret_key);
        } catch (Exception e) {
            // 예외 발생 시 런타임 예외로 변환하여 던짐
            throw new RuntimeException(e);
        }
    }
}
