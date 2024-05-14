package com.remind.core.domain.takingMedicine.repository;

import com.remind.core.domain.takingMedicine.TakingMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakingMedicineRepository extends JpaRepository<TakingMedicine, Long> {
}
