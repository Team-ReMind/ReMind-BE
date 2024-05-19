package com.remind.core.domain.member.repository;

import com.remind.core.domain.member.Doctor;
import com.remind.core.domain.member.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {

}
