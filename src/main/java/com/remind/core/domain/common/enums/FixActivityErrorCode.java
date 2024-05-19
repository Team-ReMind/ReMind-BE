package com.remind.core.domain.common.enums;

import com.remind.core.domain.common.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FixActivityErrorCode implements BaseErrorCode {
    FIX_ACTIVITY_NOT_FOUND(500, "고정 활동을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;


    FixActivityErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return null;
    }
}
