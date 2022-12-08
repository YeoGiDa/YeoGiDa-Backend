package com.Udemy.YeoGiDa.domain.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinRequest {

    @NotEmpty
    @Email
    @ApiModelProperty(example = "test3@test3")
    private String email;

    @NotEmpty
    @ApiModelProperty(example = "1234567890")
    private String kakaoId;

    @NotEmpty
    @ApiModelProperty(example = "ppirae3")
    private String nickname;
}
