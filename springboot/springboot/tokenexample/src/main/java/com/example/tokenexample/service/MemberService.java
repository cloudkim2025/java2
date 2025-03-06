package com.example.tokenexample.service;

import com.example.tokenexample.mapper.MemberMapper;
import com.example.tokenexample.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public void signUp(Member member) {
        memberMapper.saved(member);
    }
}
