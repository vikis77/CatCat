package com.qin.catcat.unite.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
// import io.jsonwebtoken.*;
import java.security.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

// import com.auth0.jwt.JWT;
// import com.auth0.jwt.algorithms.Algorithm;

import java.util.Base64;
import java.util.Date;


@Component
@Slf4j
public class JwtTokenProvider {

    @Autowired
    private UserDetailsService userDetailsService;  // Spring Security 的 UserDetailsService

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 将密钥从 Base64 编码的字符串转换为 Key 对象
    @PostConstruct
    private void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //生成Token
    public String generateToken(String username,Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        log.info("Generating token for username: {}", username);
        log.info("JWT Secret: {}", jwtSecret);
        log.info("JWT Expiration: {}", jwtExpirationInMs);

        return Jwts.builder()
                .setId(String.valueOf(userId))//设置 JWT 的唯一标识符（JTI）
                .setSubject(username)//设置 JWT 的主题（Subject）
                .setIssuedAt(new Date())//设置 JWT 的签发时间
                .setExpiration(expiryDate)// 设置 JWT 的过期时间 3600000 #生成时间+1小时
                // .claim("role","admin")//设置自定义信息
                .signWith(key)//设置签名算法和密钥，用于对 JWT 进行签名
                .compact();
    }

    // 解析 JWT
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // 使用 key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //从Token中获取userName
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    //从Token中获取userId
    public String getUserIdFromJWT(String token){
        Claims claims = parseToken(token);
        return claims.getId();
    }

    //从Token中获取过期时间
    public Date getExpirationDateFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    // 从 token 中解析出 UserDetails
    public UserDetails getUserDetailsFromToken(String token) {
        // 1. 从token中获取用户名
        String username = getUsernameFromToken(token);
        // 2. 使用 UserDetailsService 根据用户名获取用户的详细信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 3. 返回用户详情
        return userDetails;
    }

    //验证Token
    public boolean validateToken(String authToken) {
        log.info("调用JWT工具类校验");
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (JwtException ex) {
            // 捕获所有与JWT相关的异常
            log.error("JWT validation error: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error while validating JWT token", ex);
        }
        return false;
    }

}
