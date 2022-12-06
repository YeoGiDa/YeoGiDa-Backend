package com.Udemy.YeoGiDa.domain.place.response;


import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaceDetailResponseDto {

    private Long id;
    private String title;
    private String address;
    private Double star;
    private Trip trip;
    private String imgUrl;
    private String content;

//    private TripDto trip;

    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDateTime createdTime;



    @Builder
    public PlaceDetailResponseDto(Place place) {
        this.id = place.getId();
        this.address = place.getAddress();
        this.title = place.getTitle();
        this.star = place.getStar();
//        this.trip = place.getTrip().getId();
        this.content = place.getContent();
        this.imgUrl = place.getImgUrl();
        this.createdTime = place.getCreatedTime();

    }
}
