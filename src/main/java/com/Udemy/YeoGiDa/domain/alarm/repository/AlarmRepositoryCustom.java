package com.Udemy.YeoGiDa.domain.alarm.repository;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.member.entity.Member;

public interface AlarmRepositoryCustom {

    Alarm findFollowAlarmByMemberAndMakeMemberId(Member member, Long makeMemberId);

    Alarm findHeartAlarmByMemberAndTripId(Member member, Long makeMemberId);

    Alarm findCommentAlarmByMemberAndMakeMemberIdAndCommentId(Member member, Long makeMemberId, Long commentId);
}
