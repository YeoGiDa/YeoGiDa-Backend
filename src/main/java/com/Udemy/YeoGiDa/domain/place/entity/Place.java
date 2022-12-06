package com.Udemy.YeoGiDa.domain.place.entity;


import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private String title;

    private String content;

    @Transient //영속 대상 제외 어노테이션, 사용 이유 조사
    private List<Img> imgList = new ArrayList<>();

    private String address;

    private double star = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trip trip;



    @Builder
    public Place(String title, String content, String address, Double star, Trip trip, List<Img> imgList ) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.star = star;
        this.trip = trip;
        this.imgList = imgList;
    }

    public void update(String title, String content, String address, Double star, List<Img> imgList){
        this.title = title;
        this.content=content;
        this.address=address;
        this.star = star;
        this.imgList = imgList;
    }


}
