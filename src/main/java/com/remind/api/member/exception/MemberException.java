package com.remind.api.member.exception;

import com.remind.core.domain.enums.MemberErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException{

    private MemberErrorCode errorCode;

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
