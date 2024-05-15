package com.remind.api.alarm.dto.response;

import com.remind.core.domain.alarm.enums.AlarmDayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "알림 조회 model")
public record AlarmListDto(
        @Schema(description = "알람 ID")
        Long alarmId,
        @Schema(description = "알람 시간")
        LocalTime alarmTime,
        @Schema(description = "알람 요일. 매일인 경우, EVERYDAY입니다.")
        AlarmDayOfWeek alarmDayOfWeek
) {
}
