package com.remind.core.domain.alarm.repository;

import com.remind.core.domain.alarm.AlarmDay;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmDayRepository extends JpaRepository<AlarmDay, Long> {

    List<AlarmDay> findAllByAlarmId(Long alarmId);
}
