package com.Udemy.YeoGiDa.global.error.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult<T> {
    private String code;
    private String message;
    private T data;

    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

