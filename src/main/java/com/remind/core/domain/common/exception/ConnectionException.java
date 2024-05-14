package com.remind.core.domain.common.exception;

import com.remind.core.domain.enums.ConnectionErrorCode;
import com.remind.core.domain.enums.PresciptionErrorCode;
import lombok.Getter;

@Getter
public class ConnectionException extends RuntimeException{

    private ConnectionErrorCode errorCode;

    public ConnectionException(ConnectionErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
