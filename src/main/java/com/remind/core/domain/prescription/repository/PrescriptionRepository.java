package com.remind.core.domain.prescription.repository;

import com.remind.core.domain.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
      List<Prescription> findAllByDoctorId(Long DoctorId);

      Optional<Prescription> findByDoctorIdAndPatientId(Long doctorId, Long patientId);

}
