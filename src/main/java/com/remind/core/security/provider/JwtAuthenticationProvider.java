package com.remind.core.security.provider;

import com.remind.core.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtProvider jwtProvider;

    /**
     * 매개 변수 authentication의 isAuthenticated()는 false
     */
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        //token 검증 및 정보 추출 >> 인증된 Authentication instance 생성
        String token = (String) authentication.getPrincipal();
        return jwtProvider.toAuthentication(token);

    }

    /**
     * 해당 AuthenticaionProvider 실행 여부
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
