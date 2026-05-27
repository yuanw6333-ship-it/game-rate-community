package com.gamerate.utils;

import com.gamerate.common.exception.BusinessException;
import com.gamerate.common.result.ResultCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private static final String USER_ID_CLAIM = "userId";

    private static final String USERNAME_CLAIM = "username";

    @Value("${gamerate.jwt.secret:}")
    private String secret;

    @Value("${gamerate.jwt.expiration-minutes:1440}")
    private Long expirationMinutes;

    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMinutes * 60 * 1000);
        return Jwts.builder()
                .subject(username)
                .claim(USER_ID_CLAIM, userId)
                .claim(USERNAME_CLAIM, username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }

    public Long getUserId(String token) {
        Object userId = parseClaims(token).get(USER_ID_CLAIM);
        if (userId instanceof Number number) {
            return number.longValue();
        }
        if (userId instanceof String value && StringUtils.hasText(value)) {
            return Long.valueOf(value);
        }
        throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Invalid token");
    }

    public String getUsername(String token) {
        Object username = parseClaims(token).get(USERNAME_CLAIM);
        if (username instanceof String value && StringUtils.hasText(value)) {
            return value;
        }
        throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Invalid token");
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException exception) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Invalid or expired token");
        }
    }

    private SecretKey getSecretKey() {
        if (!StringUtils.hasText(secret)) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), "JWT secret is not configured");
        }
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), "JWT secret must be at least 32 bytes");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
