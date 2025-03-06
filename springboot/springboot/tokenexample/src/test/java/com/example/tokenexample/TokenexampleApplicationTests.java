package com.example.tokenexample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Spring Boot 애플리케이션의 기본 구성이 정상적으로 로드되는지 확인하는 테스트 클래스.
 */
@SpringBootTest // Spring Boot 테스트 환경을 자동으로 로드
class TokenexampleApplicationTests {

	/**
	 * 애플리케이션의 컨텍스트가 정상적으로 로드되는지 검증하는 기본 테스트.
	 * - Spring Boot의 기본 설정이 올바르게 동작하는지 확인하는 역할.
	 */
	@Test
	void contextLoads() {
		// 기본적으로 아무 작업도 수행하지 않음.
		// Spring Boot가 애플리케이션 컨텍스트(ApplicationContext)를 정상적으로 로드하면 테스트가 통과됨.
	}
}
