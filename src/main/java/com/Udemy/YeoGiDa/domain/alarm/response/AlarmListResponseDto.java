package com.Udemy.YeoGiDa.domain.alarm.response;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlarmListResponseDto {

    private String nickname;

    private String imgUrl;

    private AlarmType alarmType;

    private Long targetId;

    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime createdTime;

    @Builder
    public AlarmListResponseDto(Alarm alarm, Member member) {
        this.nickname = member.getNickname();
        this.imgUrl = member.getMemberImg().getImgUrl();
        this.alarmType = alarm.getAlarmType();
        this.targetId = alarm.getTargetId();
        this.text = alarm.getAlarmType().getAlarmText();
        this.createdTime = alarm.getCreatedTime();
    }
}
