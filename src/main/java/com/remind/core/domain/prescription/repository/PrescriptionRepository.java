package com.remind.core.domain.prescription.repository;

import com.remind.api.member.dto.PatientDto;
import com.remind.core.domain.connection.enums.ConnectionStatus;
import com.remind.core.domain.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

      Optional<Prescription> findByConnectionId(Long connectionId);

//      @Query("SELECT new Prescription" +
//              "FROM Connection c " +
//              "JOIN c.patient p " +
//              "WHERE c.targetMember.id = :targetMemberId AND c.connectionStatus = :connectionStatus")
//      Optional<Prescription> findByMemberId(@Param("memberId") Long memberId);

      //memberId로 처방 정보를 조회
      @Query("SELECT p FROM Prescription p JOIN p.connection c JOIN c.patient m WHERE m.id = :patientId")
      List<Prescription> findAllByPatientId(@Param("patientId") Long patientId);

}
