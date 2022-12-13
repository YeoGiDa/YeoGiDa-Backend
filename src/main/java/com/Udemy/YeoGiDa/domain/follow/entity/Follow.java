package com.Udemy.YeoGiDa.domain.follow.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
@IdClass(Follow.PK.class)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"toMemberId", "fromMemberId"}
                )
        })
public class Follow extends BaseEntity {

    @Id
    @Column(name = "toMemberId", insertable = false, updatable = false)
    private Long toMemberId;


    @Id
    @Column(name = "fromMemberId", insertable = false, updatable = false)
    private Long fromMemberId;

    @Builder
    public Follow(Long toMemberId, Long fromMemberId) {
        this.toMemberId = toMemberId;
        this.fromMemberId = fromMemberId;
    }

    public static class PK implements Serializable {
        Long toMemberId;
        Long fromMemberId;
    }

}
