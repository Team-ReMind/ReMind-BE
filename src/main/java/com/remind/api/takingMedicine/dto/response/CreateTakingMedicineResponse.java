package com.remind.api.takingMedicine.dto.response;

import lombok.Builder;

@Builder
public record CreateTakingMedicineResponse(
        String notTakingReason,
        Boolean isTaking
) {
}
