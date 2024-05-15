package com.remind.api.takingMedicine.dto.response;

import lombok.Builder;

@Builder
public record CheckTakingMedicineResponse(
        String notTakingReason,
        Boolean isTaking
) {
}
