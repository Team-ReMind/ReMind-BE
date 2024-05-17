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

        //약 미복용,복용 기록 정보가 없을 시 호출할 dto
        public static DailyTakingMedicineDto ofUnchecking(MedicinesType medicinesType,  int importance) {
                return DailyTakingMedicineDto.builder()
                        .medicinesType(medicinesType)
                        .importance(importance)
                        .isTaking(false)
                        .build();
        }

        //약 미복용시 호출할 dto
         public static  DailyTakingMedicineDto ofUntaking(MedicinesType medicinesType,  int importance, String notTakingReason) {
                return DailyTakingMedicineDto.builder()
                        .medicinesType(medicinesType)
                        .importance(importance)
                        .isTaking(false)
                        .notTakingReason(notTakingReason)
                        .build();
        }

        //약 복용시 호출할 dto
        public static DailyTakingMedicineDto ofTaking(MedicinesType medicinesType,  int importance,LocalTime takingTime) {
                return DailyTakingMedicineDto.builder()
                        .medicinesType(medicinesType)
                        .importance(importance)
                        .isTaking(true)
                        .takingTime(takingTime)
                        .build();
        }

}
