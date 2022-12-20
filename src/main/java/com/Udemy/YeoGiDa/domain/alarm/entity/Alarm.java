package com.Udemy.YeoGiDa.domain.alarm.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "alarm", indexes = {
        @Index(name = "member_id_idx", columnList = "member_id")
})
public class Alarm extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    //알람을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    private Long makeAlarmMemberId;

    private Long placeId;

    /*
    NEW_FOLLOW: followerId;
    NEW_COMMENT: commentId;
    NEW_HEART: tripId;
     */
    private Long targetId;

    @Builder
    public Alarm(Member member, AlarmType alarmType, Long makeAlarmMemberId,
                 Long placeId, Long targetId) {
        this.member = member;
        this.alarmType = alarmType;
        this.makeAlarmMemberId = makeAlarmMemberId;
        this.placeId = placeId;
        this.targetId = targetId;
    }
}
