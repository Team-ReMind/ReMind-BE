
package com.remind.core.domain.fcm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public record MessageDto(
        @Schema(description = "프론트에서 발급받은 디바이스 식별용 fcmToken", required = true)
        String fcmToken,
        @Schema(description = "알림 제목")
        String title,
        @Schema(description = "알림 내용")
        String body) {
}