package com.remind.core.domain.common.exception;

import com.remind.core.domain.common.enums.ActivityErrorCode;
import lombok.Getter;

@Getter
public class ActivityException extends RuntimeException {

    private ActivityErrorCode errorCode;

    public ActivityException(ActivityErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
