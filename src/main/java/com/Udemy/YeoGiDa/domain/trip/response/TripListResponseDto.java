package com.Udemy.YeoGiDa.domain.trip.response;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TripListResponseDto {

    private Long tripId;
    private Long memberId;
    private String title;
    private String subTitle;
    private String imgUrl;
    private Integer heartCount;
    private Integer placeCount;

    public TripListResponseDto(Trip trip) {
        this.tripId = trip.getId();
        this.memberId = trip.getMember().getId();
        this.title = trip.getTitle();
        this.subTitle = trip.getSubTitle();
        if(trip.getTripImg() == null) {
            this.imgUrl = null;
        } else {
            this.imgUrl = trip.getTripImg().getImgUrl();
        }
        this.heartCount = trip.getHearts().size();
        this.placeCount = trip.getPlaces().size();
    }
}
