package com.remind.core.domain.takingMedicine.repository;

import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.takingMedicine.TakingMedicine;
import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TakingMedicineRepository extends JpaRepository<TakingMedicine, Long> {
    List<TakingMedicine> findByPrescriptionIdAndDate(Long prescriptionId, LocalDate date);

    List<TakingMedicine> findAllByPrescriptionId( Long prescriptionId);

    // 환자 id와 날짜를 통해 약 복용 정보를 찾는 쿼리
    @Query("SELECT tm FROM TakingMedicine tm JOIN tm.prescription p JOIN p.connection c JOIN c.patient p2 WHERE p2.id = :patientId and tm.date = :date")
    List<TakingMedicine> findAllByDateAndMemberId(@Param("date") LocalDate date, @Param("patientId") Long patientId);

    // 환자id, 날짜, 약 시간 타입을 통해 약 정보를 찾는 쿼리
    @Query("SELECT tm FROM TakingMedicine tm " +
            "JOIN tm.prescription p " +
            "JOIN p.connection c " +
            "JOIN c.patient p2 " +
            "WHERE p2.id = :patientId and tm.date = :date AND tm.medicinesType = :medicinesType")
    List<TakingMedicine> findAllByDateAndMemberIdAndMedicinesType(@Param("date") LocalDate date,
                                                                  @Param("patientId") Long patientId,
                                                                  @Param("medicinesType") MedicinesType medicinesType);

}
