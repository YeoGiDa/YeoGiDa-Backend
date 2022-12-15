package com.Udemy.YeoGiDa.domain.place.response;


import com.Udemy.YeoGiDa.domain.place.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlaceListResponseDto {
    private Long placeId;
    private Long memberId;
    private String title;
    private Double star;
    private String imgUrl;
    private Integer commentCount;
    private String tag;

    public PlaceListResponseDto(Place place) {
        this.placeId=place.getId();
        this.memberId=place.getTrip().getMember().getId();
        this.title = place.getTitle();
        this.star = place.getStar();
        if(place.getPlaceImgs().size() > 0) {
            this.imgUrl = place.getPlaceImgs().get(0).getImgUrl();
        } else {
            this.imgUrl = null;
        }
        this.commentCount = place.getComments().size();
        this.tag = place.getTag();
    }
}
