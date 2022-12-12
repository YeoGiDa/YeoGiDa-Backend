package com.Udemy.YeoGiDa.global.jwt.service;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.global.jwt.Token;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenHasExpiredException;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenIsInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtProvider {

    private final String secret;
    private final MemberRepository memberRepository;

    private Key key;

    long tokenPeriod = 1000L * 60L * 100L; //100분
    long refreshPeriod = 1000L * 60L * 60L * 24L * 30L * 3L; //3달

    @Autowired
    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            MemberRepository memberRepository) {
        this.secret = secret;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token generateToken(Member member) {

        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put("roles", member.getRole());
        Date now = new Date();

        String accessToken = Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(key)
                        .compact();

        String refreshToken = Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(key)
                        .compact();

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .key(member.getEmail())
                .build();
    }

    public String getEmailFromAccessToken(String accessToken){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            String email = getEmailFromAccessToken(accessToken);
            boolean existMember = memberRepository.existsByEmail(email);
            if (existMember == false) {
                throw new MemberNotFoundException();
            }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            log.error("Token is Invalid");
            return false;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            log.error("Token is Invalid");
            return false;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            log.error("Token Has Expired");
            return false;
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            log.error("Token is Invalid");
            return false;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            log.error("Token is Invalid");
            return false;
        }
    }

    public String validateRefreshTokenAndReissueAccessToken(String refreshToken) {

        try {
            //검증
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

            //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성합니다.
            if(!claims.getBody().getExpiration().before(new Date())) {
                return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
            }
        } catch (Exception e){
            throw new TokenHasExpiredException();
        }
        return null;
    }

    public String recreationAccessToken(String email, Object roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();

        //AccessToken
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenPeriod))
                .signWith(key)
                .compact();

        return accessToken;
    }
}
