package com.Udemy.YeoGiDa.domain.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequest {

    @NotEmpty
    @Email
    @ApiModelProperty(example = "test3@test3")
    private String email;

    @NotEmpty
    @ApiModelProperty(example = "1234567890")
    private String kakaoId;
}
