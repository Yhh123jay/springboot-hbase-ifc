package com.yhh.springboot_hbase_ifc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;


public class JWTUtils {
    // string
    /**
     * 默认签名密钥
     */
    private static final String DEFAULT_SECRET_KEY = "guanyinshidaqiao";
    /**
     * 签发者
     */
    private static final String ISSUER = "yexijie";
    /**
     * 过期时间
     */
    private static final long EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000;

    // 生成JWT
    public static String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, DEFAULT_SECRET_KEY)
                .compact();
    }

    // 解析JWT
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(DEFAULT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }



}
