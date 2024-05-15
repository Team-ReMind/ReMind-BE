package com.remind.api.alarm.repository;

import com.remind.api.alarm.dto.response.AlarmListDto;
import com.remind.core.domain.alarm.repository.AlarmRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmListRepository extends AlarmRepository {

    @Query("select new com.remind.api.alarm.dto.response.AlarmListDto"
            + "(alarm.id,alarm.alarmTime,alarmDay.alarmDay) "
            + "from Prescription prescription "
            + "inner join Alarm alarm on prescription.id = alarm.prescription.id "
            + "inner join AlarmDay alarmDay on alarm.id = alarmDay.alarm.id "
            + "where prescription.id = :prescriptionId")
    List<AlarmListDto> getAlarms(@Param("prescriptionId") Long prescriptionId);
}
