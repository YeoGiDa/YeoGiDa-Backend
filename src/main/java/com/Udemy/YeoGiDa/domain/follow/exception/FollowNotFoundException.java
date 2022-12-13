package com.Udemy.YeoGiDa.domain.follow.exception;

public class FollowNotFoundException extends com.Udemy.YeoGiDa.global.exception.NotFoundException {

    public FollowNotFoundException() {
        super("팔로우 하지 않은 멤버입니다.");
    }

}
