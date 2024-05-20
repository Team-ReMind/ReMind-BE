package com.remind.api.takingMedicine.dto.request;

import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateTakingMedicineRequest(
        MedicinesType medicinesType,
        Boolean isTaking,
        String notTakingReason

) {
}
