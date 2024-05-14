package com.remind.core.domain.takingMedicine;

import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakingMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taking_medicine_id")
    private Long id;

    private LocalDate date;

    @Enumerated(value = EnumType.STRING)
    private MedicinesType medicinesType;

    private Boolean isTaking;

    private LocalTime takingTime;

    private String notTakingReason;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;
}
