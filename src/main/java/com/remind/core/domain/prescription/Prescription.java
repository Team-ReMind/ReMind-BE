package com.remind.core.domain.prescription;

import com.remind.core.domain.connection.Connection;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.connection.enums.ConnectionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    @Column(name = "period", nullable = false)
    private int period;

    @Column(name = "prescription_date", nullable = false)
    private LocalDate prescriptionDate;

    @Column(name = "memo")
    private String memo;

    @Column(name = "breakfast_importance", nullable = false)
    private int breakfastImportance;

    @Column(name = "lunch_importance", nullable = false)
    private int lunchImportance;

    @Column(name = "dinner_importance", nullable = false)
    private int dinnerImportance;


    @ManyToOne
    @JoinColumn(name = "connection_id")
    private Connection connection;




    /**
     * date가 처방 날짜 안에 존재하는 정보인지 확인
     * @param date
     * @return
     */
    public Boolean isDateInPrescription(LocalDate date) {
        LocalDate startDate = this.getPrescriptionDate().plus(0, ChronoUnit.DAYS);
        LocalDate endDate = this.getPrescriptionDate().plus(this.period, ChronoUnit.DAYS);


        return !date.isAfter(endDate) && !date.isBefore(startDate);
    }

    /**
     * 본 처방에 할당된 일일 약 복용 횟수
     * @return
     */
    public int takingMedicineCount() {
        return (this.breakfastImportance > 0 ? 1 : 0)
                + (this.lunchImportance > 0 ? 1 : 0)
                + (this.dinnerImportance > 0 ? 1 : 0);
    }
}
