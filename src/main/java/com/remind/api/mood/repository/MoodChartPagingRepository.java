package com.remind.api.mood.repository;

import com.remind.api.mood.dto.MoodChartDto;
import com.remind.core.domain.mood.repository.MoodRepository;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoodChartPagingRepository extends MoodRepository {

    /**
     * 환자의 특정 년도, 월의 cursor 기반 페이지네이션
     */
    @Query("select new com.remind.api.mood.dto.MoodChartDto"
            + "(mood.moodDate,mood.feelingType) "
            + "from Mood mood "
            + "where mood.patient.id = :memberId and mood.moodDate >= :moodDate "
            + "order by mood.moodDate asc")
    Slice<MoodChartDto> getMoodChartPaging2(@Param("memberId") Long memberId,
                                            @Param("moodDate") LocalDate moodDate,
                                            Pageable pageable);
}