package com.remind.core.domain.prescription;

import com.remind.core.domain.member.Member;
import com.remind.core.domain.connection.enums.ConnectionStatus;
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

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Member patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Member doctor;


    public void updatePrescriptionInfo(int period, LocalDate prescriptionDate, String memo, int breakfastImportance, int lunchImportance, int dinnerImportance, int etcImportance) {
        this.period = period;
        this.prescriptionDate = prescriptionDate;
        this.memo = memo;
        this.breakfastImportance = breakfastImportance;
        this.lunchImportance = lunchImportance;
        this.dinnerImportance = dinnerImportance;
        this.etcImportance = etcImportance;
    }
}
