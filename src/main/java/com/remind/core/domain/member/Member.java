package com.remind.core.domain.member;

import com.remind.core.domain.member.enums.RolesType;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member", indexes = {@Index(name = "idx_authId", columnList = "authId")})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private Long authId; // 소셜 로그인 고유 회원 식별 값

    private String name;

    private int age;

    private String gender;

    private String email;

    private String phoneNumber;

    private String profileImageUrl;

    // 기기 등록 토큰
    private String fcmToken;

    @Enumerated(value = EnumType.STRING)
    private RolesType rolesType;

    private String memberCode;

    //온보딩 후 rolesType  업데이트를 위한 메서드
    public void updateRolesTypeAndFcmToken(RolesType rolesType, String fcmToken) {
        this.rolesType = rolesType;
        this.fcmToken = fcmToken;
    }

}
