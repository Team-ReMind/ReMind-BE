package com.remind.api.mood.repository;

import com.remind.api.mood.dto.response.ModelActivityResponseDto;
import com.remind.core.domain.mood.repository.MoodActivityRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DateMoodActivityRepository extends MoodActivityRepository {
    /**
     * 특정 날짜의 오늘의 기분에 포함된 활동들에 대한 정보를 추출한다.
     */
    @Query("select new com.remind.api.mood.dto.response.ModelActivityResponseDto"
            + "(activity.activityName,activity.activityIcon,modelActivity.feelingType,modelActivity.moodActivityDetails) "
            + "from MoodActivity modelActivity "
            + "left join Activity activity on modelActivity.activity.id = activity.id "
            + "where modelActivity.mood.id = :moodId")
    List<ModelActivityResponseDto> getModelActivities(@Param("moodId") Long moodId);

}
