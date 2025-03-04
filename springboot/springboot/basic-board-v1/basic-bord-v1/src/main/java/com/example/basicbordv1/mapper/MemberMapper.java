package com.example.basicbordv1.mapper;

import com.example.basicbordv1.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void insertMember(Member member);
    Member selectMemberByUserId(String userId);
}
