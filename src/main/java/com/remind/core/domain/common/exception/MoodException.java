package com.remind.core.domain.common.exception;


import com.remind.core.domain.enums.MoodErrorCode;
import lombok.Getter;

@Getter
public class MoodException extends RuntimeException {

    private MoodErrorCode errorCode;

    public MoodException(MoodErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
