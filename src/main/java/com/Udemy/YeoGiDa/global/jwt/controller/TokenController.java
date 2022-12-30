package com.Udemy.YeoGiDa.global.jwt.controller;

import com.Udemy.YeoGiDa.global.jwt.exception.TokenIsInvalidException;
import com.Udemy.YeoGiDa.global.jwt.service.JwtProvider;
import com.Udemy.YeoGiDa.global.response.success.DefaultResult;
import com.Udemy.YeoGiDa.global.response.success.StatusCode;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {

    private final JwtProvider jwtProvider;

    @ApiOperation("토큰 유효성 검사 (accessToken, refreshToken) Json에 보내주세요.")
    @PostMapping("/validate")
    public ResponseEntity validateToken(@RequestBody HashMap<String, String> bodyJson) throws TokenIsInvalidException {
        log.info("refresh controller 실행");
        String oldAccessToken = bodyJson.get("accessToken");
        boolean isAccessTokenValid = false;
        isAccessTokenValid = jwtProvider.validateAccessToken(oldAccessToken);

        if(isAccessTokenValid == true) {
            log.info("accessToken 유효함 클라이언트에서 처리");

            return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                    "유효한 accessToken 입니다."), HttpStatus.OK);
        } else {
            log.info("isAccessTokenValid = {}", isAccessTokenValid);
            String refreshToken = bodyJson.get("refreshToken");
            String newAccessToken = jwtProvider.validateRefreshTokenAndReissueAccessToken(refreshToken);
            Map<String, Object> result = new HashMap<>();
            result.put("newAccessToken", newAccessToken);
            result.put("refreshToken", refreshToken);
            log.info("새로운 accessToken = {}", newAccessToken);

            return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                    "새로운 accessToken 발행 성공", result), HttpStatus.CREATED);
        }
    }
}
