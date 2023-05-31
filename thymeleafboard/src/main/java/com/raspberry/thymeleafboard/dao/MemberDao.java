package com.raspberry.thymeleafboard.dao;

import com.raspberry.thymeleafboard.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    //아이디 체크 메소드
    int idCheck(String m_id);
    //회원 가입 메소드
    void memberInsert(MemberDto member);
    //회원의 비밀번호 검색 메소드
    String selectPass(String m_id);
    //회원 정보를 가져오는 메소드(from minfo 뷰)
    MemberDto selectMember(String m_id);
    //회원 포인트 수정 메소드
    void updateMemberPoint(MemberDto member);
}
