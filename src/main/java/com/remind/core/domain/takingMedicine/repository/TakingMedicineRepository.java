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
    List<TakingMedicine> findAllByPrescriptionIdAndDate(Long prescriptionId, LocalDate date);

    List<TakingMedicine> findAllByPrescriptionId( Long prescriptionId);

    // 환자 id와 날짜를 통해 약 복용 정보를 찾는 쿼리(일일)
    @Query("SELECT tm FROM TakingMedicine tm JOIN tm.prescription p JOIN p.connection c JOIN c.patient p2 WHERE p2.id = :patientId and tm.date = :date")
    List<TakingMedicine> findAllByDateAndMemberId(@Param("date") LocalDate date, @Param("patientId") Long patientId);

    // 환자 id와 날짜를 통해 약 복용 정보를 찾는 쿼리(월 단위)
    @Query("SELECT tm FROM TakingMedicine tm " +
            "JOIN tm.prescription p " +
            "JOIN p.connection c " +
            "JOIN c.patient p2 " +
            "WHERE p2.id = :patientId " +
            "AND YEAR(tm.date) = :year " +
            "AND MONTH(tm.date) = :month")
    List<TakingMedicine> findAllByYearAndMonthAndMemberId(@Param("patientId") Long patientId,
                                                  @Param("year") int year,
                                                  @Param("month") int month);


    // 환자id, 날짜, 시간 타입을 통해 약 정보를 찾는 쿼리
    @Query("SELECT tm FROM TakingMedicine tm " +
            "JOIN tm.prescription p " +
            "JOIN p.connection c " +
            "JOIN c.patient p2 " +
            "WHERE p2.id = :patientId and tm.date = :date AND tm.medicinesType = :medicinesType")
    List<TakingMedicine> findAllByDateAndMemberIdAndMedicinesType(@Param("date") LocalDate date,
                                                                  @Param("patientId") Long patientId,
                                                                  @Param("medicinesType") MedicinesType medicinesType);

    //특정 날짜, 처방을 기준으로 isTaking = true인 row의 개수(월 단위 조회에 사용)
    int countByDateAndPrescriptionIdAndIsTakingIsTrue(LocalDate date, Long prescriptionId);
}
