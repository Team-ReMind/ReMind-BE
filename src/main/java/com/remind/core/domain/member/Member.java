package com.remind.core.domain.member;

import com.remind.core.domain.member.enums.RolesType;
import jakarta.persistence.Entity;
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

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member", indexes = {@Index(name = "idx_authId", columnList = "authId")})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authId; // 소셜 로그인 고유 회원 식별 값

    private String name;

    private int age;

    private String gender;

    private String email;

    private String phoneNumber;

    private String profileImageUrl;

    private Boolean isOnboardingFinished;

    @Enumerated(value = EnumType.STRING)
    private RolesType rolesType;

}
