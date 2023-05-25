package com.raspberry.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReplyDto {
    private int r_num;//기본키(댓글번호)
    private int r_bnum;//외래키(게시글번호)
    private String r_contents;
    private String r_id;//작성자 id

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "Asia/Seoul")
    private Timestamp r_date;
}
//댓글 데이터는 ajax에서 json 객체로 처리됨
//DTO에서 날짜 형식을 지정해야 함.