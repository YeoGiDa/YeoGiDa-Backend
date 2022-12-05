package com.Udemy.YeoGiDa.domain.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequest {

    @ApiModelProperty(example = "ppirae4")
    private String nickname;

    @ApiModelProperty(example = "imgUrl4.com")
    private String imgUrl;
}
