package com.Udemy.YeoGiDa.global.jwt.exception;

import io.jsonwebtoken.JwtException;

public class TokenHasExpiredException extends JwtException {
    public TokenHasExpiredException(){
        super("Token has expired.");
    }
}
