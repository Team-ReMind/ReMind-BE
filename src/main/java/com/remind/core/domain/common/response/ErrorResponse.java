package com.remind.core.domain.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private int errorCode;
    private String errorMessage;

}
