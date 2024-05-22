package com.remind.api.prescription.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "약 복용 정보 업데이트 응답 객체")
public record CreatePrescriptionResponseDto(
        Long PrescriptionId

) {
}
