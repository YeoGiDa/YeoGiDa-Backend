//package com.Udemy.YeoGiDa.global.slack.dto;
//
//import com.google.gson.annotations.SerializedName;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.util.StringUtils;
//
//@Getter
//@Setter
//public class SlackMessage {
//
//    @SerializedName("text")
//    private String text;
//
//    @SerializedName("channel")
//    private String channel;
//
//    @SerializedName("username")
//    private String botName;
//
//    @SerializedName("icon_emoji")
//    private String iconEmoji;
//
//    @SerializedName("icon_url")
//    private String iconUrl;
//
//    public SlackMessage(String text, String channel, String botName, String iconEmoji,
//                        String iconUrl) {
//        this.text = text;
//        this.channel = channel;
//        this.botName = botName;
//
//        if (StringUtils.hasText(iconEmoji)) {
//            this.iconEmoji = iconEmoji;
//        }
//
//        if (StringUtils.hasText(iconUrl)) {
//            this.iconUrl = iconUrl;
//        }
//    }
//}
