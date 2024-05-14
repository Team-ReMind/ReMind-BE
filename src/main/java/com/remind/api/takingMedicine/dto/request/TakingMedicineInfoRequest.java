package com.remind.api.takingMedicine.dto.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

public record TakingMedicineInfoRequest(
        @Schema(description = "약 복용 정보를 조회할 처방id")
        Long prescriptionId
) {
}
