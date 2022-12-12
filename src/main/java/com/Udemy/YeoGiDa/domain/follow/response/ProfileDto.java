package com.Udemy.YeoGiDa.domain.follow.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDto {

    Boolean isOneself;
    ProfileUserInfoDto userInfo;
}