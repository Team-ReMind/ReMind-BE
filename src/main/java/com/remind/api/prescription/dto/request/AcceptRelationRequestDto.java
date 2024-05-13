package com.remind.api.prescription.dto.request;

import lombok.Builder;

@Builder
public record AcceptRelationRequestDto(Long memberId) {
}
