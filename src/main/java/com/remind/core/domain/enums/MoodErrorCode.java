package com.remind.core.domain.enums;

import com.remind.core.domain.common.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MoodErrorCode implements BaseErrorCode {

    MOOD_ALREADY_EXIST(400, "해당 날짜의 오늘의 기분이 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    MOOD_NOT_FOUND(404, "해당 날짜에 존재하는 오늘의 기분이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MOOD_CHART_NOT_FOUND(404, "해당 년,월에 존재하는 무드 데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    MoodErrorCode(int errorCode, String errorMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public ErrorResponse getErrorResponse() {
        return null;
    }
}
