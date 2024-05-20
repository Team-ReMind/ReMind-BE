package com.remind.core.domain.member.repository;

import com.remind.core.domain.member.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CenterRepository extends JpaRepository<Center,Long> {

    // 특정 멤버와 연관된 센터를 찾는 쿼리
    @Query("SELECT center " +
            "FROM Center center " +
            "JOIN Member cmember ON center.id = cmember.id " +
            "JOIN Connection connection ON cmember.id = connection.targetMember.id " +
            "JOIN Member pmember ON connection.patient.id = pmember.id " +
            "WHERE pmember.id = :patientId ")
    List<Center> findAllCenterByPatient(@Param("patientId") Long patientId);

}