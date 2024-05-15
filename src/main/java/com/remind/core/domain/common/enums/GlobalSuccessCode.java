package com.remind.core.domain.common.enums;

import lombok.Getter;

@Getter
public enum GlobalSuccessCode {

    SUCCESS(200, "정상 처리되었습니다."),
    CREATE(201, "정상적으로 생성되었습니다.");

    private final int code;
    private final String message;

    GlobalSuccessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
