package com.remind.core.domain.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    public int errorCode;
    public String errorMessage;

}
