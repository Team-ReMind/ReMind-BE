package com.remind.api.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remind.core.domain.member.enums.RolesType;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OnboardingResponseDto(Long userId, RolesType rolesType) {
}
