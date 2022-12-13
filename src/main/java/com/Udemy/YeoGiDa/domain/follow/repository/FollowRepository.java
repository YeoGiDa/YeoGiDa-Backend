package com.Udemy.YeoGiDa.domain.follow.repository;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK>, FollowRepositoryCustom {
    Optional<Follow> findByToMemberIdAndFromMemberId(Long toMemberId, Long fromMemberId);

    List<Follow> findAll();

    Long countByToMemberId(Long memberId);

    Long countByFromMemberId(Long memberId);


}
