package com.Udemy.YeoGiDa.domain.report.controller;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.report.request.ReportRequestDto;
import com.Udemy.YeoGiDa.domain.report.service.ReportService;
import com.Udemy.YeoGiDa.global.response.DefaultResult;
import com.Udemy.YeoGiDa.global.response.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
public class RepositoryController {

    private final ReportService reportService;

    public RepositoryController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity report(@LoginMember Member member,
                                 @RequestBody ReportRequestDto reportRequestDto) {
        reportService.report(member, reportRequestDto.getType(), reportRequestDto.getTargetId());
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "슬랙 보내기 성공 "), HttpStatus.OK);
    }
}
