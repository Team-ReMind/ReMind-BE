package com.remind.api.mood.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.remind.core.domain.common.exception.ActivityException;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.MoodException;
import com.remind.core.domain.common.response.ErrorResponse;
import com.remind.core.domain.common.enums.ActivityErrorCode;
import com.remind.core.domain.common.enums.MemberErrorCode;
import com.remind.core.domain.common.enums.MoodErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MoodExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error(">>>>> Internal Server Error : {}", ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ex.getMessage());
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

    @ExceptionHandler(ActivityException.class)
    public ResponseEntity<ErrorResponse> handleActivityException(ActivityException ex) {
        log.error(">>>>> Activity Error : {}", ex);
        ActivityErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build());
    }

    @ExceptionHandler(MoodException.class)
    public ResponseEntity<ErrorResponse> handleMoodException(MoodException ex) {
        log.error(">>>>> Mood Error : {}", ex);
        MoodErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.builder()
                .errorCode(errorCode.getErrorCode())
                .errorMessage(errorCode.getErrorMessage())
                .build());
    }
}
