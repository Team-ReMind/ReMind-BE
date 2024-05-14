package com.remind.api.prescription.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record RequestRelationResponseDto(Long PrescriptionId) {
}
