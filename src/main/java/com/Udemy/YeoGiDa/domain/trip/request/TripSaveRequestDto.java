package com.Udemy.YeoGiDa.domain.trip.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TripSaveRequestDto {

    private String region;
    private String title;
    private String subTitle;
    private String imgUrl;

    @Builder
    public TripSaveRequestDto(String region, String title, String subTitle, String imgUrl) {
        this.region = region;
        this.title = title;
        this.subTitle = subTitle;
        this.imgUrl = imgUrl;
    }
}
