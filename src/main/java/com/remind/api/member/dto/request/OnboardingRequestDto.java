package com.remind.api.member.dto.request;

import com.remind.core.domain.member.enums.RolesType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "로그인이 끝난 후 온보딩에 사용하는 dto")
public record OnboardingRequestDto(
        @Schema(description = "역할 : ROLE_PATIENT or ROLE_DOCTOR or ROLE_CENTER")
        @NotNull(message = "역할은 필수 값입니다.")
        RolesType rolesType,
        @Schema(description = " ROLE_PATIENT 인 경우, 보호자 전화번호")
        String protectorPhoneNumber,
        @Schema(description = " ROLE_CENTER인 경우, 시/도")
        String city,
        @Schema(description = " ROLE_CENTER인 경우, 군/구")
        String district,
        @Schema(description = " ROLE_CENTER인 경우, 센터명")
        String centerName

) {
}