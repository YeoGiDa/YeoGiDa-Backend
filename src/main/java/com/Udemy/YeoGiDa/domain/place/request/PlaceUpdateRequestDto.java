package com.Udemy.YeoGiDa.domain.place.request;

import lombok.*;
import org.springframework.data.geo.Point;

@Getter
@Setter
@NoArgsConstructor
public class PlaceUpdateRequestDto {


    private String content;
    private Float star;
    private String tag;

    @Builder
    public PlaceUpdateRequestDto(String content, Float star, String tag) {
        this.content = content;
        this.star = star;
        this.tag = tag;
    }
}