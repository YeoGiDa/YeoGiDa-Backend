package com.Udemy.YeoGiDa.domain.trip.exception;

import com.Udemy.YeoGiDa.global.exception.NotFoundException;

public class TripNotFoundException extends NotFoundException {

    public TripNotFoundException() {
        super("회원을 찾을 수 없습니다.");
    }
}
