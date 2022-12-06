package com.Udemy.YeoGiDa.domain.place.request;

import com.Udemy.YeoGiDa.domain.place.entity.Img;
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
    private List<Img> imgUrl;
    private String content;


    @Builder
    public PlaceSaveRequestDto(String title, String address, List<Img> imgUrl, String content, Double star) {
        this.title = title;
        this.address = address;
        this.imgUrl = imgUrl;
        this.content = content;
        this.star = star;
    }
}