package com.harry.tong.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private  String secretKey = "Harry_Tong";

    private long expiration = 600000;  // 一小时

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)       // 设置 JWT 的主题，通常是用户的标识，例如用户名
                .setIssuedAt(new Date())    // 设置 JWT 的发行时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration))   // 设置 JWT 的过期时间
                .signWith(SignatureAlgorithm.HS256,secretKey)    // 设置签名算法和密钥
                .compact();     // 生成 JWT 字符串
    }

    /**
     * @param token
     * @return
     *
     * Claims 对象包含了 JWT 的所有声明，你可以从中获取具体的信息，例如用户 ID、角色等。如果你的 JWT 结构更复杂，可能需要从 Claims 对象中提取特定的数据
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)   // 用来验证签名的密钥
                .parseClaimsJws(token)      // 解析 JWT，并验证其签名
                .getBody();                 // 获取 JWT 的声明部分
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();   // 从 Claims 对象中提取主题（Subject）
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());     // 验证是否过期
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(getUsername(token)) && !isTokenExpired(token));     // 验证提取的用户名是否正确和是否过期
    }
}
