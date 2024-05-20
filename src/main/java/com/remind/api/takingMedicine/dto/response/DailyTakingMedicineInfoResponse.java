package com.remind.api.takingMedicine.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remind.api.takingMedicine.dto.DailyTakingMedicineDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "특정 날짜의 약 복용 정보를 반환할 dto list")
public record DailyTakingMedicineInfoResponse(
        @Schema(description = "특정 날짜의 약 복용 정보를 반환할 리스트")
        List<DailyTakingMedicineDto> dailyTakingMedicineDtos
) {

}
