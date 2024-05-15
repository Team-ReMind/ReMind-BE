package com.remind.api.takingMedicine.dto;

import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "특정 월에서 일 단위의 약 복용 정보를 반환할 dto")
@Builder
public record MonthlyTakingMedicineDto(

        @Schema(description = "날짜")
        LocalDate date,
        @Schema(description = "약을 복용한 횟수")
        int takingCount) {

}
