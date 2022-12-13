package com.Udemy.YeoGiDa.domain.follow.repository;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK>, FollowRepositoryCustom {
    Optional<Follow> findByToMemberIdAndFromMemberId(Long toMemberId, Long fromMemberId);

    List<Follow> findAll();

    @Query(value ="select m from Follow f INNER JOIN Member m ON f.toMemberId = m.id where f.fromMemberId = :memberId")
    List<Member> findAllByFromMemberId(@Param(value = "memberId") Long memberId);
    @Query(value ="select m from Follow f INNER JOIN Member m ON f.fromMemberId = m.id where f.toMemberId = :memberId")
    List<Member> findAllByToMemberId(@Param(value = "memberId") Long memberId);

    Long countByToMemberId(Long memberId);

    Long countByFromMemberId(Long memberId);


}
