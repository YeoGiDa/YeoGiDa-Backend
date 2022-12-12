package com.Udemy.YeoGiDa.domain.member.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Transient
    private MemberImg memberImg = new MemberImg();

    private String role;

    private String refreshToken;

    @Builder
    public Member(String email, String password, String nickname, String role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setMemberImg(MemberImg memberImg) {
        this.memberImg = memberImg;
    }
}
