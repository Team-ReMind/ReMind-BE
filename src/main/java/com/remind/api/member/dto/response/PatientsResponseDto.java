package com.remind.api.member.dto.response;

import com.remind.api.member.dto.PatientDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record PatientsResponseDto(
        @Schema(description = "관리중인 환자 목록 리스트")
        List<PatientDto> patientDtos,

        @Schema(description = "관리중인 환자 인원수")
        Integer patientNumber,

        @Schema(description = "의사/센터의 코드 번호")
        String  targetMemberCode,

        @Schema(description = "의사/센터의 담당자 이름")
        String  doctorName,

        @Schema(description = "의사/센터의 프로필사진")
        String  imageUrl
) {
}


