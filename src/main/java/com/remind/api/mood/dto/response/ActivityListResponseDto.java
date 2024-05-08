package com.remind.api.mood.dto.response;

import com.remind.core.domain.mood.Activity;
import java.util.List;

public record ActivityListResponseDto(
        List<Activity> activities
) {

}
