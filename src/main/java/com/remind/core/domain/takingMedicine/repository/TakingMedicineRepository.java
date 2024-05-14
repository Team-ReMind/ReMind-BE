package com.remind.core.domain.takingMedicine.repository;

import com.remind.core.domain.takingMedicine.TakingMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TakingMedicineRepository extends JpaRepository<TakingMedicine, Long> {
    List<TakingMedicine> findByPrescriptionIdAndDate(Long prescriptionId, LocalDate date);

    List<TakingMedicine> findAllByPrescriptionId( Long prescriptionId);
}
