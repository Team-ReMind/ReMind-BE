package com.remind.core.domain.common.exception;

import com.remind.core.domain.enums.MemberErrorCode;
import com.remind.core.domain.enums.PresciptionErrorCode;
import lombok.Getter;

@Getter
public class PrescriptionException extends RuntimeException{

    private PresciptionErrorCode errorCode;

    public PrescriptionException(PresciptionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
