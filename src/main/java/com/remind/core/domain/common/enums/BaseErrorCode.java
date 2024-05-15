package com.remind.core.domain.common.enums;

import com.remind.core.domain.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    int getErrorCode();

    String getErrorMessage();

    HttpStatus getStatus();

    ErrorResponse getErrorResponse();
}
