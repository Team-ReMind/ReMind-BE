package com.remind.api.takingMedicine.dto.request;

import lombok.Builder;

@Builder
public record CheckTakingMedicineRequest(
        String notTakingReason

) {
}
