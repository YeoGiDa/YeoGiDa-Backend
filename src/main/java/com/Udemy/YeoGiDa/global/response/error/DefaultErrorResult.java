package com.Udemy.YeoGiDa.global.error.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult<T> {
    private int code;
    private String message;
    private T data;

    public ErrorResult(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
}

