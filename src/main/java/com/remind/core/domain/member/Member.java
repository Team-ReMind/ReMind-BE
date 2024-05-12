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
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("false")
    private Boolean isOnboardingFinished;

    @Enumerated(value = EnumType.STRING)
    private RolesType rolesType;

    // 환자를 위한 컬럼
    private String protectorPhoneNumber;

    // 센터를 위한 컬럼
    private String city;

    // 센터를 위한 컬럼
    private String district;

    // 센터를 위한 컬럼
    private String centerName;

    private String fcmToken;

    //온보딩 후 특정 컬럼 업데이트를 위한 메서드 - 환자용
    public void updateRolesTypeForUser(RolesType rolesType,String protectorPhoneNumber) {
        this.isOnboardingFinished = true;
        this.rolesType = rolesType;
        this.protectorPhoneNumber = protectorPhoneNumber;
    }

    //온보딩 후 특정 컬럼 업데이트를 위한 메서드 - 센터용
    public void updateRolesTypeForCenter(RolesType rolesType,String city, String district, String centerName) {
        this.isOnboardingFinished = true;
        this.rolesType = rolesType;
        this.city = city;
        this.district = district;
        this.centerName = centerName;
    }

    //온보딩 후 특정 컬럼 업데이트를 위한 메서드 - 의사용
    public void updateRolesTypeForDoctor(RolesType rolesType) {
        this.isOnboardingFinished = true;
        this.rolesType = rolesType;
    }
}
