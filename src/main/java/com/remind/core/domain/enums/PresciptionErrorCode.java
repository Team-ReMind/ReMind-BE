package com.remind.core.domain.enums;

import com.remind.core.domain.common.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PresciptionErrorCode implements BaseErrorCode {

    MEMBER_NOT_DOCTOR(400, "요청 대상 멤버가 의사가 아닙니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_PATIENT(400, "요청 대상 멤버가 환자가 아닙니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_PRESCRIPTION_REQUEST(400, "이미 보낸 요청이 존재합니다.", HttpStatus.BAD_REQUEST),
    NO_PRESCRIPTION_REQUEST(400, "보낸 요청이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_PRESCRIPTION_ACCEPTED(400, "이미 요청이 수락되었습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_UNAUTHORIZED(401, "의사 또는 센터 관계자만 접근 가능합니다.", HttpStatus.UNAUTHORIZED),
    PRESCRIPTION_NOT_FOUND(404, "해당 정보로 약 처방 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND);


    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    PresciptionErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return null;
    }
}
