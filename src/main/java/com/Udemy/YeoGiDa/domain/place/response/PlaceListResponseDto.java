package com.Udemy.YeoGiDa.domain.place.response;


import com.Udemy.YeoGiDa.domain.place.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaceListResponseDto {

    private String title;
    private String imgUrl;
    private Double star;



//    private Integer commentCount;

    public PlaceListResponseDto(Place place){

        this.title = place.getTitle();
        this.imgUrl = place.getImgUrl();
        this.star = place.getStar();

    }


}
