package com.remind.core.domain.common.enums;

import com.remind.core.domain.common.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PresciptionErrorCode implements BaseErrorCode {

//    MEMBER_NOT_DOCTOR_OR_CENTER(400, "요청 대상 멤버가 의사 또는 센터가 아닙니다.", HttpStatus.BAD_REQUEST),
//    MEMBER_NOT_PATIENT(400, "요청 대상 멤버가 환자가 아닙니다.", HttpStatus.BAD_REQUEST),
//    DUPLICATE_CONNECTION_REQUEST(400, "이미 보낸 요청이 존재합니다.", HttpStatus.BAD_REQUEST),
//    NO_CONNECTION_REQUEST(400, "보낸 요청이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
//    ALREADY_CONNECTION_ACCEPTED(400, "이미 요청이 수락되었습니다.", HttpStatus.BAD_REQUEST),
    PRESCRIPTION_ONLY_DOCTOR(401, "약 복용 정보는 의사만 접근 가능합니다.", HttpStatus.UNAUTHORIZED),
    PRESCRIPTION_ONLY_TO_PATIENT(400, "약 복용 정보는 환자에게만 등록 가능합니다.", HttpStatus.BAD_REQUEST),
    PRESCRIPTION_ALREADY_EXIST(400, "오늘 기준으로 처방 정보가 존재합니다", HttpStatus.BAD_REQUEST),
    PRESCRIPTION_NOT_FOUND(404, "해당 정보로 약 처방 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    WRONG_MEDICINE_TYPE(500, "잘못된 약 타입.. 이거 나오면 안되는데 ㅠ", HttpStatus.INTERNAL_SERVER_ERROR);



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
