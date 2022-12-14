package com.Udemy.YeoGiDa.domain.place.response;


import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.PlaceImg;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaceDetailResponseDto {

    private Long placeId;
    private Long memberId;
    private String title;
    private String address;
    private Double star;
    private String content;
    private List<PlaceImg> placeImgs = new ArrayList<>();
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdTime;

    @Builder
    public PlaceDetailResponseDto(Place place) {
        this.placeId = place.getId();
        this.memberId= place.getTrip().getMember().getId();
        this.title = place.getTitle();
        this.address = place.getAddress();
        this.star = place.getStar();
        this.content = place.getContent();
        this.placeImgs = place.getPlaceImgs();
        this.createdTime = place.getCreatedTime();
    }
}
