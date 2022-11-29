package com.Udemy.YeoGiDa.global.jwt.service;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.global.jwt.Token;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenHasExpiredException;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenIsInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtProvider {

    private final String secret;

    private Key key;

    public JwtProvider(
            @Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token generateToken(Member member) {
        long tokenPeriod = 1000L * 60L * 10L;
        long refreshPeriod = 1000L * 60L * 60L * 24L * 30L * 3L;

        Date now = new Date();
        return new Token(
                Jwts.builder()
                        .setSubject(member.getEmail())
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(key)
                        .compact(),
                Jwts.builder()
                        .setSubject(member.getEmail())
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(key)
                        .compact());
    }

    public String getEmailFromAccessToken(String accessToken){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        return claims.getSubject();
    }

//    public boolean verifyToken(String accessToken) {
//        try {
//            Jws<Claims> claims =  Jwts.parserBuilder()
//                    .setSigningKey(key).build()
//                    .parseClaimsJws(accessToken);
//            return claims.getBody()
//                    .getExpiration()
//                    .after(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public boolean isValidateToken(String accessToken) throws TokenIsInvalidException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
//        } catch (SignatureException ex) {
//            log.error("Invalid JWT signature");
//            throw new TokenIsInvalidException();
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw new TokenIsInvalidException();
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw new TokenHasExpiredException();
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw new TokenIsInvalidException();
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw new TokenIsInvalidException();
        }
    }

}
