package com.Udemy.YeoGiDa.domain.place.response;


import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlaceListInTripResponseDto {

    private Long memberId;
    private String nickname;
    private String member_imgUrl;
    private Boolean trip_like_cheek;
    private String title;
    private String subTitle;
    private String imgUrl;
    private Integer heartCount;
    private Integer placeCount;

    public PlaceListInTripResponseDto(Trip trip) {
        this.memberId=trip.getMember().getId();
        this.nickname=trip.getMember().getNickname();
        this.member_imgUrl=trip.getMember().getMemberImg().getImgUrl();
        this.trip_like_cheek = Boolean.FALSE;
        this.title = trip.getTitle();
        this.subTitle = trip.getSubTitle();
        if(trip.getTripImg() == null) {
            this.imgUrl = null;
        } else {
            this.imgUrl = trip.getTripImg().getImgUrl();
        }
        this.heartCount = trip.getHearts().size();
        this.placeCount = trip.getPlaces().size();
    }
}