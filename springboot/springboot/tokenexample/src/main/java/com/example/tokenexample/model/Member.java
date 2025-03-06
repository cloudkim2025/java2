package com.example.tokenexample.model;

import com.example.tokenexample.type.Role;
import lombok.Builder;
import lombok.Getter;

/**
 * 애플리케이션의 사용자 정보를 담는 클래스.
 * JWT의 Payload에 저장될 사용자 정보를 포함함.
 */
@Getter
@Builder
public class Member {
    private long id;       // 사용자 ID (Primary Key)
    private String userId; // 사용자의 로그인 ID
    private String password; // 비밀번호 (보안상 안전하게 저장해야 함)
    private String userName; // 사용자의 이름
    private Role role;    // 사용자의 권한 (ex: ROLE_USER, ROLE_ADMIN 등)
}
