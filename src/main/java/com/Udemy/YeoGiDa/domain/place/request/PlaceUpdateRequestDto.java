package com.Udemy.YeoGiDa.domain.place.request;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter
@Setter
@NoArgsConstructor
public class PlaceUpdateRequestDto {

    private String title;
    private String address;
    private Double longitude;
    private Double latitude;
    private String content;
    private Float star;
    private String tag;

    @Builder
    public PlaceUpdateRequestDto(String title, String address,  Double longitude, Double latitude, String content, Float star, String tag) {
        this.title = title;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.content = content;
        this.star = star;
        this.tag = tag;
    }
}