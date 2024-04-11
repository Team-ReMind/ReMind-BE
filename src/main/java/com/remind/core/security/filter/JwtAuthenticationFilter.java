package com.remind.core.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.core.security.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Jwt token을 authenticate하는 필터입니다.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final String AUTHENTICATION_HEADER = "Authorization";
    private final String AUTHENTICATION_SCHEME = "Bearer "; // token prefix
    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractHeader(request); // 헤더에서 token 추출 및 prefix 제거
        Authentication authentication = jwtProvider.toAuthentication(token); // token 검증 및 정보 추출 >> Authentication 객체 생성

        // security context에 저장 >> 추후 @@AuthenticationPrincipal를 통해 조회 가능
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 다음 필터로 proceed
        filterChain.doFilter(request, response);


    }


    private String extractHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHENTICATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHENTICATION_SCHEME)) {
            return bearerToken.substring(AUTHENTICATION_SCHEME.length());  // 'Bearer ' prefix 제거
        }
        throw new PreAuthenticatedCredentialsNotFoundException("토큰이 존재하지 않습니다.");
    }
}
