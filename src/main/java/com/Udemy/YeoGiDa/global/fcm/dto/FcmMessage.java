package com.Udemy.YeoGiDa.global.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {
    private boolean validate_only;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
//        private Notification notification;
        private String token;
        private Map<String, Object> data;
    }

//    @Builder
//    @AllArgsConstructor
//    @Getter
//    public static class Notification {
//        private String title;
//        private String body;
//        private String image;
//    }
}
