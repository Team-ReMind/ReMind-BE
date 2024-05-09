package com.remind.api.mood.dto.response;

import com.remind.api.mood.dto.ActivityListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
@Schema(description = "추가한 활동 목록")
public record ActivityListResponseDto(
        @Schema(description = "환자가 추가한 활동들 목록")
        List<ActivityListDto> activities
) {

}
