package com.Udemy.YeoGiDa.domain.trip.response;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TripListResponseDto {

    private String title;
    private String subTitle;
    private String imgUrl;
    private Integer heartCount;
    //private Integer placeCount;

    public TripListResponseDto(Trip trip) {
        this.title = trip.getTitle();
        this.subTitle = trip.getSubTitle();
        this.imgUrl = trip.getImgUrl();
        this.heartCount = trip.getHeartCount();
//        this.placeCount = trip.getPlaceCount();
    }
}
