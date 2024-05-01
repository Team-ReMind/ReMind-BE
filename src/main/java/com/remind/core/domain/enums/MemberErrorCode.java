package com.remind.core.domain.enums;

import com.remind.core.domain.common.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements BaseErrorCode {

    REFRESH_TOKEN_NOT_FOUND(404, "해당 memberId로 저장된 refresh token이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    REFRESH_TOKEN_NOT_MATCH(404, "저장된 refresh token 값과 일치하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND(404, "일치하는 member가 없습니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    MemberErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return null;
    }
}
