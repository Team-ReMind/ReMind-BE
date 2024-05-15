package com.remind.api.prescription.dto.response;

import com.remind.api.prescription.dto.PrescriptionDto;
import lombok.Builder;

import java.util.List;

@Builder
public record PrescriptionInfoResponseDto(
       List<PrescriptionDto> prescriptionDtos

) {
}
