package com.example.tokenexample.config.security;

import com.example.tokenexample.model.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security의 UserDetails 인터페이스를 구현한 클래스.
 * 인증된 사용자 정보를 포함하며, Spring Security에서 사용자 인증 및 권한 부여에 활용됨.
 */
@Getter
@Builder
public class CustomUserDetails implements UserDetails {

    private Member member; // 사용자 정보를 담고 있는 Member 객체
    private List<String> roles; // 사용자의 역할(Role) 목록

    /**
     * 사용자의 권한(Authorities) 목록을 반환하는 메서드.
     * SimpleGrantedAuthority 객체로 변환하여 Spring Security에서 권한 정보를 활용할 수 있도록 처리.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new) // 역할(Role)을 SimpleGrantedAuthority로 변환
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 비밀번호를 반환하는 메서드.
     * Security 인증 과정에서 사용됨.
     */
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /**
     * 사용자의 고유한 아이디(로그인 ID)를 반환하는 메서드.
     * Security에서 사용자 식별자로 사용됨.
     */
    @Override
    public String getUsername() {
        return member.getUserId();
    }

    /**
     * 계정이 만료되지 않았는지를 반환하는 메서드.
     * `true`이면 계정이 활성 상태이며, `false`이면 만료된 상태로 간주됨.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않도록 기본적으로 `true` 설정
    }

    /**
     * 계정이 잠겨있지 않았는지를 반환하는 메서드.
     * `true`이면 계정이 잠겨있지 않은 상태이며, `false`이면 계정이 잠긴 상태로 간주됨.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않도록 기본적으로 `true` 설정
    }

    /**
     * 사용자의 인증 정보(비밀번호)가 만료되지 않았는지를 반환하는 메서드.
     * `true`이면 인증 정보가 유효한 상태이며, `false`이면 인증 정보가 만료된 상태로 간주됨.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호가 만료되지 않도록 기본적으로 `true` 설정
    }

    /**
     * 사용자가 활성화되었는지를 반환하는 메서드.
     * `true`이면 계정이 활성화된 상태이며, `false`이면 계정이 비활성화된 상태로 간주됨.
     */
    @Override
    public boolean isEnabled() {
        return true; // 계정이 비활성화되지 않도록 기본적으로 `true` 설정
    }
}
