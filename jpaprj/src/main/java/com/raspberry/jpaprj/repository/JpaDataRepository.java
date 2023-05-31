package com.raspberry.jpaprj.repository;

import com.raspberry.jpaprj.entity.JpaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaDataRepository extends JpaRepository<JpaData, Long> {
    //DB CRUD(insert, update, delete, select 용 인터페이스)
    //기본으로 전체 내용 삽입, 전체 내용 수정, 행 삭제
    //전체 내용 검색 및 키 검색용 메소드를 제공.
    List<JpaData> findByStrdata(String strdata);
    //SELECT * FROM jpatbl WHERE str_data = 'abcd'
}
