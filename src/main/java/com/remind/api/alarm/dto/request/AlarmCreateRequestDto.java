package com.remind.api.alarm.dto.request;

import com.remind.core.domain.alarm.enums.AlarmDayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


@Schema(description = "알림 생성 요청 model")
public record AlarmCreateRequestDto(
        @Schema(description = "처방전 ID")
        Long prescriptionId,
        @Schema(description = "알람 시간의 시 값입니다. 오후 9시이면 21입니다.")
        String alarmHour,
        @Schema(description = "알람 시간의 분 값입니다. 오후 9시30분이면 30입니다.")
        String alarmMinute,
        @Schema(description = "알림 요일들. 매일인 경우에는 EVERYDAY 하나만 주면 됩니다.")
        List<AlarmDayOfWeek> alarmDaysOfWeek
) {
}
