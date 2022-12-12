package com.Udemy.YeoGiDa.domain.follow.response;

import com.Udemy.YeoGiDa.domain.follow.entity.FollowStatus;
import lombok.Data;

@Data
public class UserSimpleInfoDto {

    private String requestingUsername;
    private FollowStatus followStatus;

    private Long memberId;
    private String nickname;

    public UserSimpleInfoDto(Long memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
