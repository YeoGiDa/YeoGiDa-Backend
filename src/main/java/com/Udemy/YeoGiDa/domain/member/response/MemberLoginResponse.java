package com.Udemy.YeoGiDa.domain.member.response;

import com.Udemy.YeoGiDa.global.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberLoginResponse {

    private Long memberId;
    private Token token;
}
