package com.Udemy.YeoGiDa.domain.member.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class MemberImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_img_id")
    private Long id;

    private String imgUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public MemberImg(String imgUrl, Member member) {
        this.imgUrl = imgUrl;
        this.member = member;
    }
}
