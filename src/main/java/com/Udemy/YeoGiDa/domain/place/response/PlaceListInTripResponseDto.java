package com.Udemy.YeoGiDa.domain.place.response;


import com.Udemy.YeoGiDa.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlaceListInTripResponseDto {

    private String title;
    private String subTitle;
    private String imgUrl;
    private Integer heartCount;
    private Integer placeCount;


    public PlaceListInTripResponseDto(Place place) {
        this.title = place.getTrip().getTitle();
        this.subTitle = place.getTrip().getSubTitle();
        if(place.getTrip().getTripImg() == null) {
            this.imgUrl = null;
        } else {
            this.imgUrl = place.getTrip().getTripImg().getImgUrl();
        }
        this.heartCount = place.getTrip().getHearts().size();
        this.placeCount = place.getTrip().getPlaces().size();
    }
}
