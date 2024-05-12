package com.remind.core.domain.mood.repository;

import com.remind.core.domain.mood.Activity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    /**
     * member id를 이용하여 해당 맴버가 추가한 활동을 조회하는 쿼리
     */
    @Query("select activity from Activity activity where activity.member.id = :memberId")
    List<Activity> findActivitiesByMemberId(@Param("memberId") Long memberId);
}
