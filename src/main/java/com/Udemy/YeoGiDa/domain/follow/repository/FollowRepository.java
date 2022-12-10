//package com.Udemy.YeoGiDa.domain.follow.repository;
//
//import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
//import com.Udemy.YeoGiDa.domain.follow.response.FollowResponseDto;
//import com.Udemy.YeoGiDa.domain.member.entity.Member;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface FollowRepository extends JpaRepository<Follow,Follow.PK> {
//
//    Optional<Follow> findByToMemberAndFromMember(Long toMemberId, Long fromMemberId);
//    Long countByToMember(Long MemberId);
//    Long countByFromMember(Long MemberId);
//
//    @Query(value = "select m from Follow f INNER JOIN Member m ON f.toMember = m.id where f.fromMember = :memberId")
//    List<FollowResponseDto> findAllByFromMember(@Param("memberId") Long memberId);  // 내가 팔로우한 관계를 가져옴
//
//    @Query(value = "select m from Follow f INNER JOIN Member m ON f.fromMember = m.id where f.toMember = :memberId")
//    List<FollowResponseDto> findAllByToMember(@Param("memberId") Long memberId);	  // 나를 팔로우하는 관계를 가져옴
//
//    void deleteAllByFromUser(Long userId);
//    void deleteAllByToUser(Long userId);
//
//}
