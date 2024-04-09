package com.remind.core.domain.member.enums;

import lombok.Getter;

@Getter
public enum RolesType {

    ROLE_USER,  // 일반 회원(환자)
    ROLE_DOCTOR, // 의사
    ROLE_CENTER  // 센터 관리자

}
