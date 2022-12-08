package com.Udemy.YeoGiDa.domain.place.request;

import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaceSaveRequestDto {

    private String title;
    private String address;
    private Double longitude;
    private Double latitude;
    private Double star;
    private String content;
    private String tag;

    @Builder
    public PlaceSaveRequestDto(String title, String address, String content,  Double longitude, Double latitude,Double star, String tag) {
        this.title = title;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.content = content;
        this.star = star;
        this.tag = tag;
    }
}