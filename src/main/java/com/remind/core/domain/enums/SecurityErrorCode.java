package com.remind.core.domain.enums;

import com.remind.core.domain.common.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SecurityErrorCode implements BaseErrorCode {


    TOKEN_EXPIRED(401, "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    SecurityErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return null;
    }
}
