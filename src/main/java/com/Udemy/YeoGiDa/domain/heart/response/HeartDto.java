package com.Udemy.YeoGiDa.domain.heart.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartDto {

    private Long memberId;
    private Long tripId;
}
