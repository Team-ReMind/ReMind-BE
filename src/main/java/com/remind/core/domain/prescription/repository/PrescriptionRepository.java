package com.remind.core.domain.prescription.repository;

import com.remind.core.domain.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {


      //memberId로 처방 정보를 조회
      @Query("SELECT p FROM Prescription p JOIN p.connection c JOIN c.patient m WHERE m.id = :patientId")
      List<Prescription> findAllByPatientId(@Param("patientId") Long patientId);

      //멤버가 가진 처방 중에서, 오늘 날짜가 포함된 처방을 찾기
      @Query(nativeQuery = true, value = """
              SELECT p.*
              FROM prescription p
              JOIN connection c ON p.connection_id = c.connection_id
              JOIN member m ON c.patient_id  = m.member_id
              WHERE m.member_id  = :patientId
              AND :date BETWEEN p.prescription_date AND DATE_ADD(p.prescription_date, INTERVAL p.period DAY);
              """)
      Optional<Prescription> findByPatientIdAndValidDate(@Param("patientId") Long patientId, @Param("date") LocalDate date);





}
