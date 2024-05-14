package com.remind.core.domain.member.enums;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum RolesType {

    ROLE_PATIENT,  // 일반 회원(환자)
    ROLE_DOCTOR, // 의사
    ROLE_CENTER,  // 센터 관리자
    ROLE_UNREGISTER; //온보딩 미진행

    public static Boolean validateUserRole(Collection<? extends GrantedAuthority> roles) {
        return roles.stream().anyMatch(role -> role.getAuthority().equals(ROLE_PATIENT.name()));
    }
}
