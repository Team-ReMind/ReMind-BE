package com.remind.core.domain.common.response;

import static com.remind.core.domain.enums.GlobalSuccessCode.SUCCESS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remind.core.domain.enums.GlobalSuccessCode;
import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ApiSuccessResponse <T> {

    private int code;
    private String message;
    private T data;

    public ApiSuccessResponse(T data) {
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.data = data;
    }

    public ApiSuccessResponse(GlobalSuccessCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }
}
