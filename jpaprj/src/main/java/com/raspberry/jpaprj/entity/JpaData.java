package com.raspberry.jpaprj.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name="jpatbl")
@Data
public class JpaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(name = "str_data", nullable = false, length = 50) //nullable=false : null을 허용하지 않는다.
    private String strdata;

    @Column(name = "int_data")
    private int intdata;

    @Column(name = "reg_date")
    @CreationTimestamp //작성일 기준으로 db에 저장한다.
    private Timestamp regdate;
}
