package com.remind.api.mood.repository;

import com.remind.core.domain.mood.enums.FeelingType;
import com.remind.core.domain.mood.repository.MoodRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoodConsecutiveRepository extends MoodRepository {

    // 밋업데이 MVP 단계에서는 없어진 비즈니스 로직(현재까지 최대 연속 일수)이다.

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


    /**
     * 현재 날짜부터 시작하여 날짜(mood_date)를 기준으로 연속인 데이터의 갯수를 구한다. 사용자 정의 변수(@count,@prev_date)를 사용한다. 이때, 날짜 기준으로 정렬된
     * 테이블(sorted_mood_table)에서 하나의 행씩 가져와 이전 행의 날짜(@prev_date)와 연속인지 비교(DATE_SUB())하여 연속이면 @count를 하나씩 증가시키고 연속이지 않으면
     * NULL로 초기화한다.
     * 한번 @count 값이 null로 초기화되면(연속 x) 그 아래 데이터들의 COUNT 컬럼은 모두 NULL 값이다.
     */
    @Query(nativeQuery = true, value = """
                 SELECT MAX(count_table.COUNT)
                 FROM (
                     SELECT
                         @count \\:= IF(@count is NOT NULL and sorted_mood_table.mood_date = DATE_SUB(@prev_date, INTERVAL 1 DAY), @count + 1, NULL) as COUNT,
                         @prev_date \\:= sorted_mood_table.mood_date as PREV_DATE
                     FROM (
                             SELECT mood_date
                             FROM mood m
                             WHERE m.patient_id = 1
                             ORDER BY m.mood_date desc
            ) AS sorted_mood_table, (SELECT @prev_date \\:= DATE_ADD(CURRENT_DATE(), INTERVAL 1 DAY), @count \\:= 0) AS variables
                     ) AS count_table
                     WHERE count_table.COUNT IS NOT NULL; 
                 """)
    Optional<Long> getCurrentSeries(@Param("patientId") Long patientId);


    /**
     * 오늘 날짜(nowDate)와 기준 날짜(criterionDate)사이의 무드 점수를 조회한다.
     */
    @Query("select mood.feelingType "
            + "from Mood mood "
            + "where mood.patient.id = :patientId and (mood.moodDate between :nowDate and :criterionDate)")
    List<FeelingType> getMoodFeelingTypes(@Param("patientId") Long patientId,
                                          @Param("nowDate") LocalDate nowDate,
                                          @Param("criterionDate") LocalDate criterionDate);
}
