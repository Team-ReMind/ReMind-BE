package com.remind.core.domain.member.repository;

import com.remind.api.member.dto.PatientDto;
import com.remind.core.domain.connection.enums.ConnectionStatus;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {

}
