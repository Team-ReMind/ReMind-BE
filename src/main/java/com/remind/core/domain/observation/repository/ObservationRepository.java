package com.remind.core.domain.observation.repository;

import com.remind.core.domain.observation.Observation;
import com.remind.core.domain.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findAllByCenterId(Long CenterId);


}
