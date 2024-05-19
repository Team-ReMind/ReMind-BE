package com.remind.core.domain.common.exception;

import com.remind.core.domain.common.enums.FixActivityErrorCode;
import lombok.Getter;

@Getter
public class FixActivityException extends RuntimeException {

    private FixActivityErrorCode errorCode;

    public FixActivityException(FixActivityErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
