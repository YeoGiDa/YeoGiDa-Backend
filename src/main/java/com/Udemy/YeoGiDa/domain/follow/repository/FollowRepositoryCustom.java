package com.Udemy.YeoGiDa.domain.follow.repository;

public interface FollowRepositoryCustom {

    int findSizeFollower(Long memberId);

    int findSizeFollowing(Long memberId);
}
