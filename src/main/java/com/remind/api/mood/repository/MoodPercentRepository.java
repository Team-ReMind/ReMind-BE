package com.remind.api.mood.repository;

import com.remind.core.domain.mood.enums.FeelingType;
import com.remind.core.domain.mood.repository.MoodActivityRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoodPercentRepository extends MoodActivityRepository {

    /**
     * 한 환자의 전체 활동에 대한 기분 조회
     */
    @Query("select moodActivity.feelingType "
            + "from Member member "
            + "left join Mood mood on member.id = mood.patient.id "
            + "left join MoodActivity moodActivity on mood.id = moodActivity.mood.id "
            + "where member.id = :memberId")
    List<FeelingType> getActivityFeelingTypePercent(@Param("memberId") Long memberId);
}
