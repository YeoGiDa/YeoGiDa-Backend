package com.Udemy.YeoGiDa.global.fcm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestDto {
    private String title;
    private String body;
    private String targetToken;
}
