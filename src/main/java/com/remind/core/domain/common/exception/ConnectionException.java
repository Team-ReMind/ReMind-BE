package com.remind.core.domain.common.exception;

import com.remind.core.domain.common.enums.ConnectionErrorCode;
import lombok.Getter;

@Getter
public class ConnectionException extends RuntimeException{

    private ConnectionErrorCode errorCode;

    public ConnectionException(ConnectionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
