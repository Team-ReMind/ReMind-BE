package com.remind.core.domain.member.repository;

import com.remind.core.domain.member.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    // 특정 멤버와 연관된 의사를 찾는 쿼리
    @Query("SELECT doctor " +
            "FROM Doctor doctor " +
            "JOIN Member dmember ON doctor.id = dmember.id " +
            "JOIN Connection connection ON dmember.id = connection.targetMember.id " +
            "JOIN Member pmember ON connection.patient.id = pmember.id " +
            "WHERE pmember.id = :patientId ")
    List<Doctor> findAllDoctorByPatient(@Param("patientId") Long patientId);

}
