package com.Udemy.YeoGiDa.domain.report.service;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.report.entity.Report;
import com.Udemy.YeoGiDa.domain.report.repository.ReportRepository;
import com.Udemy.YeoGiDa.domain.report.request.ReportRequestDto;
import com.Udemy.YeoGiDa.global.slack.service.SlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final SlackService slackService;

    public ReportService(ReportRepository reportRepository, SlackService slackService) {
        this.reportRepository = reportRepository;
        this.slackService = slackService;
    }

//    public void report(Member member, String type, Long targetId) {
//        reportRepository.save(new Report(member, type, targetId));
//
//        StringBuilder sb = new StringBuilder();
//        String message = "";
//        if (type.equals("MEMBER")) {
//            Member findMember = memberRepository.findById(targetId).orElseThrow(MemberNotFoundException::new);
//            sb.append(":alert:[REPORT]").append(System.getProperty("line.separator"))
//                    .append("[Member_ID] : ").append(findMember.getId()).append(System.getProperty("line.separator"))
//                    .append("[Nickname] : ").append(findMember.getNickname()).append(System.getProperty("line.separator"))
//                    .append("[ImgUrl] : ").append(findMember.getMemberImg().getImgUrl());
//        } else if (type.equals("TRIP")) {
//            Trip trip = tripRepository.findById(targetId).orElseThrow(TripNotFoundException::new);
//            sb.append(":alert:[REPORT]").append(System.getProperty("line.separator"));
//                sb.append("[Trip_ID] : ").append(trip.getId()).append(System.getProperty("line.separator"))
//                        .append("[Trip Title] : ").append(trip.getTitle()).append(System.getProperty("line.separator"))
//                        .append("[Trip SubTitle] : ").append(trip.getSubTitle()).append(System.getProperty("line.separator"))
//                        .append("[Trip ImgUrl] : ").append(trip.getTripImg().getImgUrl()).append(System.getProperty("line.separator"));
//        } else if (type.equals("PLACE")) {
//            Place place = placeRepository.findById(targetId).orElseThrow(PlaceNotFoundException::new);
//            sb.append(":alert:[REPORT]").append(System.getProperty("line.separator"));
//                sb.append("[Place_ID] : ").append(place.getId()).append(System.getProperty("line.separator"))
//                        .append("[Place Title] : ").append(place.getTitle()).append(System.getProperty("line.separator"))
//                        .append("[Place Content] : ").append(place.getContent()).append(System.getProperty("line.separator"))
//                        .append("[Place ImgUrl] : ").append(place.getPlaceImgs().get(0).getImgUrl()).append(System.getProperty("line.separator"));
//        } else if (type.equals("COMMENT")) {
//            Comment comment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
//            sb.append(":alert:[REPORT]").append(System.getProperty("line.separator"))
//                    .append("[Comment_ID] : ").append(comment.getId()).append(System.getProperty("line.separator"))
//                    .append("[CONTENT] : ").append(comment.getContent()).append(System.getProperty("line.separator"));
//        }
//        message = sb.toString();
//        slackSender.sendSlack(message);
//    }

    public void report(Member member, ReportRequestDto reportRequestDto) throws IOException {
        reportRepository.save(new Report(member, reportRequestDto.getType(), reportRequestDto.getTargetId()));

        slackService.stackBlock(reportRequestDto);
    }

}
