package com.remind.core.security.jwt;

import com.remind.core.security.dto.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
     */
    public String createAccessToken(UserDetailsImpl userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofSeconds(accessExpirationSeconds).toMillis());

        String authorities = "";

        if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
            authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
        }

        return Jwts.builder()
                .claim("memberId", userDetails.getMemberId())
                .claim("name", userDetails.getName())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim(AUTHORIZATION_ROLE, authorities)
                .signWith(createKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * refresh token 생성
     */
    public String createRefreshToken(UserDetailsImpl userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofSeconds(refreshExpirationSeconds).toMillis());

        String authorities = "";

        if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
            authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
        }

        return Jwts.builder()
                .claim("memberId", userDetails.getMemberId())
                .claim("name", userDetails.getName())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim(AUTHORIZATION_ROLE, authorities)
                .signWith(createKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 정보 추출
     */
    public Authentication toAuthentication(String token) {

        Claims claims = validate(token).getBody();

        Object roles = claims.get(AUTHORIZATION_ROLE);
        Set<GrantedAuthority> authorities = null;
        if (roles != null && !roles.toString().trim().isEmpty()) {
            authorities = Set.of(new SimpleGrantedAuthority(roles.toString()));
        }
        UserDetails user = UserDetailsImpl.builder()
                .memberId(claims.get("memberId", Long.class))
                .name(claims.get("name", String.class))
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }


    /**
     * 토큰 검증
     */
    public Jws<Claims> validate(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(createKey())
                .build();
        return jwtParser.parseClaimsJws(token);
    }

    private Key createKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


}
