package com.example.tokenexample.config;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HMAC SHA-512 알고리즘을 사용하여 Secret Key를 생성하는 테스트 클래스.
 * JWT 서명(Signature)에 사용할 보안 키를 생성하는 용도로 활용 가능.
 */
class SecretKeyGen {

    /**
     * HMAC SHA-512 방식으로 비밀 키(Secret Key)를 생성하는 테스트 메서드.
     * 생성된 Secret Key는 JWT의 서명(Signature) 키로 사용할 수 있음.
     */
    @Test
    void HS512_생성() {
        Mac sha512_HMAC = null; // HMAC-SHA512 알고리즘을 적용할 Mac 객체
        String data = "Spring boot basic board 2"; // 해싱할 데이터 (예제용)
        String secretKey = "256-bit-secret"; // 비밀키 (실제 서비스에서는 더 강력한 키를 사용해야 함)

        try {
            // HMAC SHA-512 알고리즘을 사용하도록 설정
            sha512_HMAC = Mac.getInstance("HmacSHA512");

            // SecretKeySpec을 사용하여 키를 바이트 배열로 변환 후, 알고리즘 설정
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA512");

            // HMAC 객체에 비밀 키 설정
            sha512_HMAC.init(keySpec);

            // 입력 데이터(data)를 HMAC SHA-512로 해싱하여 MAC 데이터 생성
            byte[] macData = sha512_HMAC.doFinal(data.getBytes("UTF-8"));

            // 생성된 바이트 데이터를 Base64 문자열로 변환
            String secret_key = Base64.getEncoder().encodeToString(macData);

            // 생성된 Secret Key 출력
            System.out.println(secret_key);
        } catch (Exception e) {
            throw new RuntimeException(e); // 예외 발생 시 런타임 예외로 변환하여 던짐
        }
    }
}
