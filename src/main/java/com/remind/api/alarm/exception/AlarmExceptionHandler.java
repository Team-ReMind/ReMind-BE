package com.remind.api.alarm.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.remind.core.domain.common.enums.AlarmErrorCode;
import com.remind.core.domain.common.enums.MemberErrorCode;
import com.remind.core.domain.common.enums.PresciptionErrorCode;
import com.remind.core.domain.common.exception.AlarmException;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AlarmExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error(">>>>> Internal Server Error : {}", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }


    @ExceptionHandler(AlarmException.class)
    public ResponseEntity<ErrorResponse> handleAlarmException(AlarmException ex) {
        log.error(">>>>> Alarm Error : {}", ex);
        AlarmErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build());
    }

    @ExceptionHandler(PrescriptionException.class)
    public ResponseEntity<ErrorResponse> handlePrescriptionException(PrescriptionException ex) {
        log.error(">>>>> Alarm Error : {}", ex);
        PresciptionErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build());
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException ex) {
        log.error(">>>>> Member Error : {}", ex);
        MemberErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build());
    }
}
