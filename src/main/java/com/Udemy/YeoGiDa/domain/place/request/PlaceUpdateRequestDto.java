package com.Udemy.YeoGiDa.domain.place.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PlaceUpdateRequestDto {

    private String title;

    private String address;

    private String imgUrl;

    private String content;

    private Double star;

    @Builder
    public PlaceUpdateRequestDto(String title, String address, String imgUrl, String content, Double star) {
        this.title = title;
        this.address = address;
        this.imgUrl = imgUrl;
        this.content = content;
        this.star = star;
    }
}
