package com.Udemy.YeoGiDa.global.jwt;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Token {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String key;
}
