### JPA(Java Persistence API)
    자바 어플리케이션에서 관계형 데이터베이스를 사용하는
    방식을 정의한 인터페이스 관련 API.

    관계형 데이터베이스(RDB)
        테이블(엔티티)과 테이블 간의 관계로 데이터를 저장하는 방식의 데이터베이스
        - MySQL, 오라클, 마리아DB 등.

        Hibernate : JPA의 구현체 (JPA는 인터페이스, Hibernate는 구현 클래스)

        Spring Data JPA : JPA와 Hibernate를 사용하기 쉽게 만든 Spring 라이브러리.

        Entity 클래스(DTO)를 구현하면 해당 클래스에서 지정한 테이블 이름 및 컬럼 이름으로
        DB 테이블을 자동으로 생성하며, DB CRUD에 대한 메소드도 제공.
        메소드 이름으로 SQL 쿼리문을 생성하는 방식을 사용.

application.properties 설정
- DevTools(Thymeleaf) 설정
- static resource
- Datasource(DB) 설정
- JPA 설정
- DB log 설정

### JPA 초기화 전략 설정(a.k.a JPA 설정)
    spring.jpa.generate-ddl : true로 설정하면 해당 데이터를 근거로 서버 시작 시에 DDL문을 생성하여 DB에 적용.
                              DDL 생성 시 데이터베이스 고유의 기능을 사용하는지에 대한 유무 체크.
                              → false로 설정.

    spring.jpa.hibernate.ddl-auto : 'create table' 관련 설정.
        - none : 아무런 작업도 하지 않음.(DB에 테이블을 따로 생성)
        - create : 서버가 시작할 때 기본 테이블을 DROP(제거)하고 새 DDL을 실행(테이블 재생성)
        - create-drop : 서버가 시작할 때 DROP 및 CREATE하고, 서버가 종료될 때 DROP 실행.
        - update : 기존 테이블에 해당하는 Entity 클래스가 변경되면 기존 테이블을 DROP하고,
        새 클래스에 맞게 테이블 생성. 변경된 내용이 없으면 테이블 유지.
        - validate : Entity와 테이블이 잘 맵핑되어 있는지 확인하여, 맞지 않을 경우 프로그램을 종료시킴.

    spring.jpa.database-platform
        각 DBMS에 맞게 SQL을 생성하도록 도와주는 dialect(방언) 객체를 지정.

### 간단한 테이블 명세
테이블명 : jpatbl
컬럼 >
    - code : 자동 증가, 기본키
    - strdata : 문자열 저장. 길이 50자. 필수입력
    - intdata : 정수 저장. 널 허용.
    - regdate : 저장 날짜시간 저장. 기본값.

Entity class
    DB 테이블과 연계하기 위한 클래스.
    DTO의 역할도 함께 처리할 수 있음.(따로 작성하는 경우가 일반적임)

    사용하는 어노테이션
    1) @Entity : entity임을 선언하는 어노테이션(DB DDL 생성 시 활요)
    2) @Table : DB 테이블의 이름을 지정하는 어노테이션. 생략 시 클래스 이름으로 테이블 생성.
        (명시적으로 작성해 주는 것이 좋음)
        테이블명은 스네이크케이스('_')를 사용하는 것을 권장.
        (클래스명 카멜케이스, 테이블 스테네이크케이스)
    3) @Id : 필드를 테이블의 기본키로 설정하는 어노테이션
    4) @GeneratedValue : 자동으로 생성되는 키값에 대한 설정.
        MySQL의 Auto-Increment는 GenerationType.IDENTITY로 설정.
        (DBMS마다 설정 값이 다름. 오라클은 GenerationType.AUTO)
    5) @Column : 필드를 테이블의 컬럼으로 설정하는 어노테이션.
        (생략 시 필드명으로 컬럼명 생성)
        다음 옵션으로 여러 설정을 할 수 있음.
        - name : 필드명과 다르게 컬럼명을 지정.
        - nullable : false -> not null. 생략 시 null 허용 컬럼.
        - length : 컬럼의 길이 지정
        - columnDefinition : 그 외 컬럼의 제약 조건 설정.
            예) 날짜 타입의 기본값을 설정하는 경우
            columnDefinition = "DATE DEFAULT CURRENT_DATE"
    6) @ColumnDefault("값") : 컬럼에 기본값을 설정. 문자열.
        예) 정수형 컬럼에 기본값 0
            @ColumnDefault("0")
    7) @CreationTimestamp/@UpdateTimestamp
        insert 또는 update 날짜 시간을 기본값으로 설정.
        (datetime 타입에서 사용)
    8) @Transient : 컬럼으로 생성하지 않는 필드에 붙이는 어노테이션.

Repository 인터페이스
    DAO 역할을 하는 인터페이스.
    이 인터페이스 내부에 다양한 작업을 위한 메소드를 작명 규칙에 맞게 작성한다.
    작명 규칙 참고 사이트 : https://zara49.tistory.com/130
