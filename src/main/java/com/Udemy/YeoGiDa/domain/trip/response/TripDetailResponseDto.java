package com.Udemy.YeoGiDa.domain.trip.response;

import com.Udemy.YeoGiDa.domain.member.response.MemberDto;
import com.Udemy.YeoGiDa.domain.trip.entity.Region;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class TripDetailResponseDto {

    private Long id;
    private Region region;
    private String title;
    private String subTitle;
    private MemberDto member;
    private String imgUrl;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime modifiedTime;

    public TripDetailResponseDto(Trip trip) {
        this.id = trip.getId();
        this.region = trip.getRegion();
        this.title = trip.getTitle();
        this.subTitle = trip.getSubTitle();
        this.member = new MemberDto(trip.getMember());
        this.imgUrl = trip.getImgUrl();
        this.createdTime = trip.getCreatedTime();
        this.modifiedTime = trip.getModifiedTime();
    }
}
