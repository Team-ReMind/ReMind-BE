package com.remind.api.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remind.api.member.dto.PatientDto;
import com.remind.api.mood.dto.MoodChartDto;
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
        String  targetMemberCode

) {
}


