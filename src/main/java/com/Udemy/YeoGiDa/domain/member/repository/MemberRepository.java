package com.Udemy.YeoGiDa.domain.member.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByEmail(String email);
    Member findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    Optional<Member> findMemberByEmail(String email);
}
