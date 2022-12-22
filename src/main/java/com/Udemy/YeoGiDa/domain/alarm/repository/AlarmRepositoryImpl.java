package com.Udemy.YeoGiDa.domain.alarm.repository;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.Udemy.YeoGiDa.domain.alarm.entity.QAlarm.alarm;

@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Alarm findFollowAlarmByMemberAndMakeMemberId(Member member, Long makeMemberId) {
        return queryFactory.selectFrom(alarm)
                .where(alarm.alarmType.eq(AlarmType.NEW_FOLLOW),
                        alarm.member.eq(member),
                        alarm.makeAlarmMemberId.eq(makeMemberId))
                .fetchOne();
    }

    @Override
    public Alarm findHeartAlarmByMemberAndTripId(Member member, Long tripId) {
        return queryFactory.selectFrom(alarm)
                .where(alarm.alarmType.eq(AlarmType.NEW_HEART),
                        alarm.member.eq(member),
                        alarm.targetId.eq(tripId))
                .fetchOne();
    }

    @Override
    public Alarm findCommentAlarmByMemberAndMakeMemberIdAndCommentId(Member member, Long makeMemberId,
                                                                     Long placeId, Long commentId) {
        return queryFactory.selectFrom(alarm)
                .where(alarm.alarmType.eq(AlarmType.NEW_COMMENT),
                        alarm.member.eq(member),
                        alarm.makeAlarmMemberId.eq(makeMemberId),
                        alarm.placeId.eq(placeId),
                        alarm.targetId.eq(commentId))
                .fetchOne();
    }
}
