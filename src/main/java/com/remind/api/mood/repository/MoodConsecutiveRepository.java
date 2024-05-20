package com.remind.api.mood.repository;

import com.remind.core.domain.mood.repository.MoodRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoodConsecutiveRepository extends MoodRepository {

    /**
     * 날짜(mood_date)를 기준으로 연속인 데이터의 최대 갯수를 구한다. 사용자 정의 변수(@count,@prev_date)를 사용한다. 이때, 날짜 기준으로 정렬된
     * 테이블(sorted_mood_table)에서 하나의 행씩 가져와 이전 행의 날짜(@prev_date)와 연속인지 비교(DATE_ADD())하여 연속이면 @count를 하나씩 증가시키고 연속이지 않으면
     * 1로 초기화한다.
     */
    @Query(nativeQuery = true, value = """
            SELECT MAX(COUNT)
            FROM (
                SELECT
                    @count \\:= IF(sorted_mood_table.mood_date = DATE_ADD(@prev_date, INTERVAL 1 DAY), @count + 1, 1) as COUNT,
                    @prev_date \\:= sorted_mood_table.mood_date as PREV_DATE
                FROM (
                        SELECT mood_date
                        FROM mood m
                        WHERE m.patient_id = :patientId
                        ORDER BY m.mood_date
                        ) AS sorted_mood_table, (SELECT @prev_date \\:= NULL, @count \\:= 1) AS variables
                ) AS count_table;
            """)
    Long getMaxSeries(@Param("patientId") Long patientId);
}
