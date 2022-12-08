package com.Udemy.YeoGiDa.domain.place.response;


import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PlaceDetailResponseDto {

    private String title;
    private String address;
    private Double star;
    private String content;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdTime;

    @Builder
    public PlaceDetailResponseDto(Place place) {
        this.title = place.getTitle();
        this.address = place.getAddress();
        this.star = place.getStar();
        this.content = place.getContent();
        this.createdTime = place.getCreatedTime();
    }
}
