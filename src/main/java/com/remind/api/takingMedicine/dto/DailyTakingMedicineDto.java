package com.remind.api.takingMedicine.dto;

import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalTime;

@Schema(description = "특정 날짜의 약 복용 정보를 반환할 dto")
@Builder
public record DailyTakingMedicineDto(
        @Schema(description = "복용 엔티티의 식별 id")
        Long takingMedicineId,
        @Schema(description = "약의 종류(BREAKFAST, LUNCH, DINNER")
        MedicinesType medicinesType,
        @Schema(description = "처방 약의중요도")
        int importance,
        @Schema(description = "약 복용 여부")
        Boolean isTaking,
        @Schema(description = "약 먹은 시각")
        LocalTime takingTime,
        @Schema(description = "약을 먹지 않은 이유")
        String notTakingReason) {

}
