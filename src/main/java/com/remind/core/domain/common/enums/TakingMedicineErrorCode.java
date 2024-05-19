package com.remind.core.domain.common.enums;

import com.remind.core.domain.common.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TakingMedicineErrorCode implements BaseErrorCode {

    TAKING_MEDICINE_NOT_FOUND(404, "해당 정보로 복용 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    TAKING_MEDICINE_ALREADY_EXIST(400, "이미 약 복용 정보를 등록하셨습니다", HttpStatus.BAD_REQUEST);




    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    TakingMedicineErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return null;
    }
}
