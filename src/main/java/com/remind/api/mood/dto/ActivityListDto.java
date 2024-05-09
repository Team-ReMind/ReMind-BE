package com.remind.api.mood.dto;

import com.remind.core.domain.mood.Activity;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "추가한 활동 리스트 model")
public record ActivityListDto(
        @Schema(description = "활동명")
        String name,
        @Schema(description = "아이콘 이미지")
        String iconImage
) {

    public static ActivityListDto fromEntity(Activity activity){
        return new ActivityListDto(activity.getActivityName(),activity.getActivityIcon());
    }
}
