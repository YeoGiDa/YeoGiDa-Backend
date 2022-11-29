package com.Udemy.YeoGiDa.domain.trip.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trip extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long id;

    private String region;

    private String title;

    private String subTitle;

    private Integer changeNum = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private String imgUrl;

    @Builder
    public Trip(String region, String title, String subTitle, Member member, String imgUrl) {
        this.region = region;
        this.title = title;
        this.subTitle = subTitle;
        this.member = member;
        this.imgUrl = imgUrl;
    }

    public void update(String region, String title, String subTitle, String imgUrl) {
        this.region = region;
        this.title = title;
        this.subTitle = subTitle;
        this.imgUrl = imgUrl;
    }
}
