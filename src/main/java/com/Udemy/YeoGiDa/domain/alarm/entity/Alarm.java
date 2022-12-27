package com.Udemy.YeoGiDa.domain.alarm.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    private Long makeAlarmMemberId;

    /*
    NEW_FOLLOW: followerId;
    NEW_COMMENT: commentId;
    NEW_HEART: tripId;
     */
    private Long targetId;

    //빌더를 빼고 생성자
    @Builder
    public Alarm(Member member, AlarmType alarmType, Long makeAlarmMemberId, Long targetId) {
        this.member = member;
        this.alarmType = alarmType;
        this.makeAlarmMemberId = makeAlarmMemberId;
        this.targetId = targetId;
    }
}
