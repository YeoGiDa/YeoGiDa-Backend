package com.Udemy.YeoGiDa.domain.place.request;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter
@Setter
@NoArgsConstructor
public class PlaceUpdateRequestDto {

    private String title;
    private String address;
    private String content;
    private Double star;

    private Point location;

    private String tag;

    @Builder
    public PlaceUpdateRequestDto(String title, String address, String content, Double star,Point location, String tag) {
        this.title = title;
        this.address = address;
        this.content = content;
        this.location = location;
        this.star = star;
        this.tag = tag;
    }
}
