package com.example.basicbordv1.service;

import com.example.basicbordv1.mapper.MemberMapper;
import com.example.basicbordv1.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public void signUp(Member member) {
        memberMapper.insertMember(member);
    }

}