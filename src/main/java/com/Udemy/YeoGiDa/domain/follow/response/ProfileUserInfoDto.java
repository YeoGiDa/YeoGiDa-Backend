package com.Udemy.YeoGiDa.domain.follow.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProfileUserInfoDto {

    private boolean isFollowing;
    private String requestingUsername;
    private Long memberId;
    @NotBlank
    private String memberNickname;


    @NotBlank
    private Long followerCount;
    @NotBlank
    private Long followingCount;
}
