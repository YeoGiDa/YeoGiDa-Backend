package com.Udemy.YeoGiDa.domain.member.response;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class BestTravlerListResponse {

    private Long id;
    private String nickname;
    private String imgUrl;
    private int heartCount;

    @Builder
    public BestTravlerListResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        if(member.getMemberImg() == null) {
            imgUrl = null;
        } else {
            this.imgUrl = member.getMemberImg().getImgUrl();
        }
        this.heartCount = member.getTotalHeartCount();
    }
}
