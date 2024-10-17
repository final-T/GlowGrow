package com.tk.gg.security.jwt;

import com.tk.gg.common.enums.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;


@Component
public class JwtProvider {
    private final Key accessKey;
    private final Key refreshKey;
    @Getter
    private final String grantType = "Bearer";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public JwtProvider() {
        try {
            Properties properties = new Properties();
            properties.load(getClass()
                    .getClassLoader()
                    .getResourceAsStream("token-secret.properties"));

            this.accessKey = decodeKey(properties,"jwt.access.key");
            this.refreshKey = decodeKey(properties,"jwt.refresh.key");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");


        if (authorizationHeader != null && authorizationHeader.startsWith(grantType+" "))
            return Optional.of(authorizationHeader.substring(grantType.length()+1));

        return Optional.empty();
    }

    private Key decodeKey(Properties properties, String propertyKey) {
        return Keys.hmacShaKeyFor(properties.getProperty(propertyKey).getBytes());
    }

    public TokenInfo generateToken(TokenableInfo payload) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime accessExpiredTime = now.plusHours(24); // TODO: 원복하기
        LocalDateTime refreshExpiredTime = now.plusDays(30);

        String accessToken = Jwts.builder()
                .setSubject(payload.getId().toString())
                .claim("email", payload.getEmail())
                .claim("username", payload.getUsername())
                .claim("role", payload.getUserRole())
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(accessExpiredTime))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(payload.getId().toString())
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(refreshExpiredTime))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(grantType)
                .build();
    }

    public TokenStatus validateAccessToken(String token) {
        return validateToken(accessKey, token);
    }

    public TokenStatus validateRefreshToken(String token) {
        return validateToken(refreshKey, token);
    }

    private TokenStatus validateToken(Key key, String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (Exception e) {
            return TokenStatus.INVALID;
        }
    }

    public TokenableInfo parseAccessToken(String token) {
        Claims claims = getClaims(accessKey, token);
        if (claims == null)
            return null;

        return TokenableUser.builder()
                .id(Long.valueOf(claims.getSubject()))
                .email((String) claims.get("email"))
                .username((String) claims.get("username"))
                .userRole(UserRole.valueOf((String) claims.get("role")))
                .build();
    }

    private Claims getClaims(Key key, String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
