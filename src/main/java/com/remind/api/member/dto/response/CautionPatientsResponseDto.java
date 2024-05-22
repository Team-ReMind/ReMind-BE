package com.remind.api.member.dto.response;

import com.remind.api.member.dto.CautionPatientDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "주의가 필요한 환자 목록 리스트")
public record CautionPatientsResponseDto(
        @Schema(description = "관리중인 환자 목록 리스트")
        List<CautionPatientDto> cautionPatientDtos,

        @Schema(description = "관리중인 환자 인원수")
        Integer patientNumber

) {

}
