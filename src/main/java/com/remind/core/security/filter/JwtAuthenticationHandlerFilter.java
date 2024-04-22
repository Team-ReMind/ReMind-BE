package com.remind.core.security.filter;

import static com.remind.core.domain.enums.SecurityErrorCode.INVALID_TOKEN;
import static com.remind.core.domain.enums.SecurityErrorCode.TOKEN_EXPIRED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remind.core.domain.common.response.ErrorResponse;
import com.remind.core.domain.enums.SecurityErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JwtAuthenticationFilter에서 발생된 에러를 handle하는 filter입니다.
 */
@Slf4j
@NoArgsConstructor
public class JwtAuthenticationHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.error("토큰 만료 \n{}", ex.getStackTrace());
            setErrorResponse(response, TOKEN_EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException ex) {
            log.error("유효하지 않는 토큰 \n{}", ex.getStackTrace());
            setErrorResponse(response, INVALID_TOKEN);
        } catch (PreAuthenticatedCredentialsNotFoundException ex) {
            log.error("존재하지 않는 토큰 \n{}", ex.getStackTrace());
            setErrorResponse(response, ex);
        } catch (AuthenticationException ex) {
            log.error("Authentication Error 발생 \n{}", ex.getStackTrace());
            setErrorResponse(response, ex);
        } catch (AccessDeniedException ex) {
            log.error("AccessDenied Error 발생 \n{}", ex.getStackTrace());
            setErrorResponse(response, ex);
        }

    }

    private void setErrorResponse(HttpServletResponse response, SecurityErrorCode errorCode) {

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getErrorCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(objectMapper.writeValueAsString(
                    ErrorResponse.builder()
                            .errorCode(errorCode.getErrorCode())
                            .errorMessage(errorCode.getErrorMessage())
                            .build()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setErrorResponse(HttpServletResponse response, RuntimeException ex) {

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(objectMapper.writeValueAsString(
                    ErrorResponse.builder()
                            .errorCode(HttpStatus.UNAUTHORIZED.value())
                            .errorMessage(ex.getMessage())
                            .build()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
