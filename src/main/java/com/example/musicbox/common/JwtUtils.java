package com.example.musicbox.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


/**
 * JWT: token
 */
public class JwtUtils {
    //12小时过期
    private final static long expire = 43200;
    //秘钥
    private final static String secret = "asjSADo18v4SA#D3A8_1d@9iFSAdaFi12a+90FXia1!8D1d23ads";

    //生成token
    public static String generateToken(String str) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * expire);
        return Jwts.builder()
                .setHeaderParam("type","JWT")
                .setSubject(str)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    //解析token 得Claims对象 需在调用getSubject()方法解析成String
    public static Claims getClaimsByToken(String token) throws Exception{
        return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
    }

    public static String decodeByToken(String token) throws Exception {
        return getClaimsByToken(token).getSubject();
    }
}
