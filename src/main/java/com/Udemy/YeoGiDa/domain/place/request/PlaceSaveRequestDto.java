package com.Udemy.YeoGiDa.domain.place.request;


import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaceSaveRequestDto {

    private String title;
    private String address;
    private Double star;
    private String content;
    private String imgUrl;

    private Trip trip;

    @Builder
    public PlaceSaveRequestDto(String title, String address, String content, Double star,Trip trip, String imgUrl) {
        this.title = title;
        this.address = address;
        this.content = content;
        this.star = star;
        this.trip = trip;
        this.imgUrl=imgUrl;
    }
}