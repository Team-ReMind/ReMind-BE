package com.remind.core.security.jwt;

import com.remind.core.security.dto.UserDetailsImpl;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final String AUTHORIZATION_ROLE = "authorities";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-expiration-seconds}")
    private int accessExpirationSeconds;

    @Value("${jwt.refresh-expiration-seconds}")
    private int refreshExpirationSeconds;

    /**
     * access token 생성
     * @param authentication
     * @return
     */
    public String createAccessToken(Authentication authentication) {  // Authentication >> isAuthenticated()가 true
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofSeconds(accessExpirationSeconds).toMillis());

        String authorities = "";
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        }

        return Jwts.builder()
            .claim("id", userDetails.getMemberId())
            .setSubject(userDetails.getUsername())
            .setIssuedAt(now)
            .setExpiration(expiration)
            .claim(AUTHORIZATION_ROLE, authorities)
            .signWith(createKey(),SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * refresh token 생성
     * @param authentication
     * @return
     */
    public String createRefreshToken(Authentication authentication) {  // Authentication >> isAuthenticated()가 true
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofSeconds(refreshExpirationSeconds).toMillis());

        String authorities = "";
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        }

        return Jwts.builder()
            .claim("id", userDetails.getMemberId())
            .setSubject(userDetails.getUsername())
            .setIssuedAt(now)
            .setExpiration(expiration)
            .claim(AUTHORIZATION_ROLE, authorities)
            .signWith(createKey(),SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 토큰 검증
     * @param token
     */

    public void validate(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
            .setSigningKey(createKey())
            .build();

        jwtParser.parseClaimsJws(token);
    }

    private Key createKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


}
