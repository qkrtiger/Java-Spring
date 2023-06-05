package com.raspberry.movieinfo.repository;

import com.raspberry.movieinfo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    //DB CRUD(insert, update, delete, select 용 인터페이스)
    //기본으로 전체 내용 삽입, 전체 내용 수정, 행 삭제
    //전체 내용 검색 및 키 검색용 메소드를 제공.

    //페이지 처리
    Page<Movie> findByMcodeGreaterThan(Long mcode, Pageable pb);
}
