package com.remind.api.alarm.dto.response;

import com.remind.core.domain.alarm.enums.AlarmDayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "알람 목록 조회 응답 model")
public record AlarmListResponseDto(
        @Schema(description = "알람 ID")
        Long alarmId,
        @Schema(description = "알람 시간. HH:mm 형태입니다.", pattern = "HH:mm")
        String alarmTime,
        @Schema(description = "알람 요일 목록")
        List<AlarmDayOfWeek> alarmDayOfWeeks
) {
}
