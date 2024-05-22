package com.remind.api.takingMedicine.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record CreateTakingMedicineResponse(
        String notTakingReason,
        Boolean isTaking
) {
}
