package com.example.tokenexample.mapper;


import com.example.tokenexample.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void saved(Member member);
    Member findByUserId(String userId);
}
