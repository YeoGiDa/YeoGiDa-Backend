package com.Udemy.YeoGiDa.domain.follow.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FollowResponseDto {

    private String nickName;
    private String imgUrl;

    @Builder
    public FollowResponseDto(String nickName, String imgUrl) {
        this.nickName = nickName;
        this.imgUrl = imgUrl;
    }
}
