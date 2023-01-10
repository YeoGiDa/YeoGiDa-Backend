package com.Udemy.YeoGiDa.domain.alarm.service;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
import com.Udemy.YeoGiDa.domain.alarm.response.AlarmListResponseDto;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public List<AlarmListResponseDto> alarmList(Member member) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        List<Alarm> alarms = alarmRepository.findAllByMember(member);
        List<AlarmListResponseDto> alarmListResponseDtos = new ArrayList<>();
        for (Alarm alarm : alarms) {
            Long makeAlarmMemberId = alarm.getMakeMemberId();
            Member makeMember = memberRepository.findById(makeAlarmMemberId)
                            .orElseThrow(MemberNotFoundException::new);
            alarmListResponseDtos.add(new AlarmListResponseDto(alarm, makeMember));
        }
        Collections.reverse(alarmListResponseDtos);
        return alarmListResponseDtos;
    }

    public void deleteAlarmsAfterOneWeek() {
        List<Alarm> all = alarmRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Alarm alarm : all) {
            LocalDateTime createdTime = alarm.getCreatedTime();
            if(createdTime.plusDays(7).isBefore(now)) {
                alarmRepository.delete(alarm);
            }
        }
    }
}
