package com.remind.api.takingMedicine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Schema(description = "특정 날짜의 약 복용 정보를 반환할 dto")
@Builder
public record DailyTakingMedicineDto(
        @Schema(description = "해당 약 처방 정보의 식별 id")
        Long prescriptionId,
        @Schema(description = "약의 종류(BREAKFAST, LUNCH, DINNER")
        MedicinesType medicinesType,
        @Schema(description = "처방 약의중요도")
        int importance,
        @Schema(description = "약 복용 여부")
        Boolean isTaking,
        @Schema(description = "약 먹은 시각")
        String takingTime,
        @Schema(description = "약을 먹지 않은 이유")
        String notTakingReason) {

        //약 미복용,복용 기록 정보가 없을 시 호출할 dto
        public static DailyTakingMedicineDto ofUnchecking(Long prescriptionId, MedicinesType medicinesType,  int importance) {
                return DailyTakingMedicineDto.builder()
                        .prescriptionId(prescriptionId)
                        .medicinesType(medicinesType)
                        .importance(importance)
                        .isTaking(null)
                        .build();
        }

        //약 미복용시 호출할 dto
         public static  DailyTakingMedicineDto ofUntaking(Long prescriptionId, MedicinesType medicinesType,  int importance, String notTakingReason) {
                return DailyTakingMedicineDto.builder()
                        .prescriptionId(prescriptionId)
                        .medicinesType(medicinesType)
                        .importance(importance)
                        .isTaking(false)
                        .notTakingReason(notTakingReason)
                        .build();
        }

        //약 복용시 호출할 dto
        public static DailyTakingMedicineDto ofTaking(Long prescriptionId, MedicinesType medicinesType,  int importance,LocalTime takingTime) {
                return DailyTakingMedicineDto.builder()
                        .prescriptionId(prescriptionId)
                        .medicinesType(medicinesType)
                        .importance(importance)
                        .isTaking(true)
                        .takingTime(DailyTakingMedicineDto.localTimeToString(takingTime))
                        .build();
        }

        private static String localTimeToString(LocalTime time) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm");
                return  time.format(formatter).replace("AM", "오전").replace("PM", "오후");
        }

}
