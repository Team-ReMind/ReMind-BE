package com.remind.core.security.annotation;

import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.security.dto.UserDetailsImpl;
import java.util.Set;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    public static Long TEST_MEMBER_ID = 1L;
    public static String TEST_MEMBER_NAME = "이상민";
    public static RolesType TEST_MEMBER_ROLE_TYPE = RolesType.ROLE_USER;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .memberId(TEST_MEMBER_ID)
                .name(TEST_MEMBER_NAME)
                .authorities(Set.of(new SimpleGrantedAuthority(TEST_MEMBER_ROLE_TYPE.name())))
                .build();

        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        return context;
    }
}

