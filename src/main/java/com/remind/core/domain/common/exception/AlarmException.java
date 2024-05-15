package com.remind.core.domain.common.exception;

import com.remind.core.domain.common.enums.AlarmErrorCode;
import lombok.Getter;

@Getter
public class AlarmException extends RuntimeException {

    private AlarmErrorCode errorCode;

    public AlarmException(AlarmErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
