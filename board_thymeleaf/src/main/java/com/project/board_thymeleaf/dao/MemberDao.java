package com.project.board_thymeleaf.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    //아이디 체크 메소드
    int idCheck(String m_id);


}
