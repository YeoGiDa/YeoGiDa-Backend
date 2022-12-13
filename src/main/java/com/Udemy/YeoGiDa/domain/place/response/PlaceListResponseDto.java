package com.Udemy.YeoGiDa.domain.place.response;


import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.PlaceImg;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlaceListResponseDto {

    private String title;
    private Double star;
    private String imgUrl;

//    private Integer commentCount;

    public PlaceListResponseDto(Place place) {
        this.title = place.getTitle();
        this.star = place.getStar();
        if(place.getPlaceImgs().size() > 0) {
            this.imgUrl = place.getPlaceImgs().get(0).getImgUrl();
        } else {
            this.imgUrl = null;
        }
    }
}
