package com.Udemy.YeoGiDa.domain.report.service;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.comment.exception.CommentNotFoundException;
import com.Udemy.YeoGiDa.domain.comment.repository.CommentRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceRepository;
import com.Udemy.YeoGiDa.domain.report.entity.Report;
import com.Udemy.YeoGiDa.domain.report.repository.ReportRepository;
import com.Udemy.YeoGiDa.domain.trip.repository.TripRepository;
import com.Udemy.YeoGiDa.global.slack.service.SlackSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final PlaceRepository placeRepository;
    private final CommentRepository commentRepository;
    private final SlackSender slackSender;

    public ReportService(ReportRepository reportRepository, MemberRepository memberRepository,
                         TripRepository tripRepository, PlaceRepository placeRepository,
                         CommentRepository commentRepository, SlackSender slackSender) {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.placeRepository = placeRepository;
        this.commentRepository = commentRepository;
        this.slackSender = slackSender;
    }

    public void report(Member member, String type, Long targetId) {
        reportRepository.save(new Report(member, type, targetId));

        StringBuilder sb = new StringBuilder();
        String message = "";
        if(type.equals("MEMBER")) {
            Member findMember = memberRepository.findById(targetId).orElseThrow(MemberNotFoundException::new);
            sb.append("[REPORT]").append(System.getProperty("line.separator"))
                    .append("[Member_ID] : ").append(findMember.getId()).append(System.getProperty("line.separator"))
                    .append("[Nickname] : ").append(findMember.getNickname()).append(System.getProperty("line.separator"))
                    .append("[ImgUrl] : ").append(findMember.getMemberImg().getImgUrl());
        } else if(type.equals("TRIP")) {
            List<Place> placeList = placeRepository.findAllByTripId(targetId);
            sb.append("[REPORT]").append(System.getProperty("line.separator"));
            for (Place place : placeList) {
                sb.append("[Place_ID] : ").append(place.getId()).append(System.getProperty("line.separator"))
                        .append("[Place Title] : ").append(place.getTitle()).append(System.getProperty("line.separator"))
                        .append("[Place Content] : ").append(place.getContent()).append(System.getProperty("line.separator"))
                        .append("[Place ImgUrl] : ").append(place.getPlaceImgs().get(0).getImgUrl()).append(System.getProperty("line.separator"));
            }
        } else if(type.equals("COMMENT")) {
            Comment comment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
            sb.append("[REPORT]").append(System.getProperty("line.separator"))
                    .append("[Comment_ID] : ").append(comment.getId()).append(System.getProperty("line.separator"))
                    .append("[CONTENT] : ").append(comment.getContent()).append(System.getProperty("line.separator"));
        }
        message = sb.toString();
        slackSender.sendSlack(message);
        log.debug("message={}", message);
    }
}
