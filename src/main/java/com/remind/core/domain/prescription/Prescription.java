package com.remind.core.domain.prescription;

import com.remind.core.domain.member.Member;
import com.remind.core.domain.prescription.enums.RelationsType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long id;

    @Column(name = "period")
    private int period;

    @Column(name = "prescription_date")
    private LocalDate prescriptionDate;

    @Column(name = "memo")
    private String memo;

    @Column(name = "breakfast_importance")
    private int breakfastImportance;

    @Column(name = "lunch_importance")
    private int lunchImportance;

    @Column(name = "dinner_importance")
    private int dinnerImportance;

    @Column(name = "etc_importance")
    private int etcImportance;

    @Enumerated(value = EnumType.STRING)
    private RelationsType relationsType;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Member patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Member doctor;
}
