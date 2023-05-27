package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member); //저장소에 회원 저장
    Optional<Member> findById(Long id); //id를 찾는 기능
    Optional<Member> findByName(String name); //이름을 찾는 기능
    List<Member> findAll(); //전체 정보를 찾는 기능
}
