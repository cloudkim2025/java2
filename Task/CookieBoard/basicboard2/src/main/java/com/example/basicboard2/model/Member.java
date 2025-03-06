package com.example.basicboard2.model;

import com.example.basicboard2.type.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {
    private long id;
    private String userId;
    private String password;
    private String userName;
    private Role role;
}
