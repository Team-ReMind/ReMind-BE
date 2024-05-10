package com.remind.api.mood.repository;

import com.remind.api.mood.dto.MoodChartDto;
import com.remind.core.domain.mood.repository.MoodRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoodChartPagingRepository extends MoodRepository {

    /**
     * 환자의 특정 년도, 월의 무드 데이터를 가져오고 오름차순으로 정렬한다.
     */
    @Query("select new com.remind.api.mood.dto.MoodChartDto"
            + "(mood.moodDate,mood.feelingType) "
            + "from Mood mood "
            + "where mood.patient.id = :memberId and year(mood.moodDate) =:yearCursor and month(mood.moodDate) = :monthCursor "
            + "order by mood.moodDate asc")
    List<MoodChartDto> getMoodChartPaging(@Param("memberId") Long memberId,
                                          @Param("yearCursor") Integer yearCursor,
                                          @Param("monthCursor") Integer monthCursor);
}