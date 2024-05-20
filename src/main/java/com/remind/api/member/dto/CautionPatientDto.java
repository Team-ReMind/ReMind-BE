package com.remind.api.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CautionPatientDto(
        @Schema(description = "환자 아이디")
        Long memberId,

        @Schema(description = "환자 이름")
        String name,

        @Schema(description = "약 복용률// 0:엑스, 1:세모, 2:체크 ")
        int takingMedicineRatio,

        @Schema(description = "무드차트 평균 점수// 0:(0점~25점), 1:(25점~50점), 2:(50점~75점))")
        int moodChartScore,

        @Schema(description = "환자 보호자 번호")
        String protectorPhoneNumber,

        @Schema(description = "환자 번호")
        String phoneNumber


) {
}
