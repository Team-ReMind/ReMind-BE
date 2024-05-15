package com.remind.core.domain.alarm.repository;

import com.remind.core.domain.alarm.Alarm;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findAllByPrescriptionId(Long prescriptionId);
}
