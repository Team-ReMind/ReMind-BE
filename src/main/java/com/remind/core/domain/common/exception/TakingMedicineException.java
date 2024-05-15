package com.remind.core.domain.common.exception;

import com.remind.core.domain.common.enums.TakingMedicineErrorCode;
import lombok.Getter;

@Getter
public class TakingMedicineException extends RuntimeException{

    private TakingMedicineErrorCode errorCode;

    public TakingMedicineException(TakingMedicineErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
