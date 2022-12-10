//package com.Udemy.YeoGiDa.domain.follow.entity;
//
//import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
//import com.Udemy.YeoGiDa.domain.member.entity.Member;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Getter
//@Entity
//@NoArgsConstructor
//@IdClass(Follow.PK.class)
//@Table(name = "follow",
//        uniqueConstraints = {
//                @UniqueConstraint(
//                        columnNames = {"fromMemberId", "toMemberId"}
//                )
//        })
//public class Follow extends BaseEntity {
//
//    @Id
//    @Column(name = "fromMemberId", insertable = false, updatable = false)
//    private Long fromMember;
//
//
//    @Id
//    @Column(name = "toMemberId", insertable = false, updatable = false)
//    private Long toMember;
//
//    @Builder
//    public Follow(Long fromMember, Long toMember) {
//        this.fromMember = fromMember;
//        this.toMember = toMember;
//    }
//
//    public static class PK implements Serializable {
//        Long fromMember;
//        Long toMember;
//    }
//
//}
