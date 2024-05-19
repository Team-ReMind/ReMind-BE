package com.remind.core.domain.member;

import com.remind.api.takingMedicine.dto.response.UpdateTakingMedicineResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Member member;

    private String protectorPhoneNumber;

    private Double breakfastTakingMedicineRate;
    private Double lunchTakingMedicineRate;
    private Double dinnerTakingMedicineRate;
    private Double totalTakingMedicineRate;

    public void updateTakingMedicineRate(Double breakfastTakingMedicineRate, Double lunchTakingMedicineRate, Double dinnerTakingMedicineRate, Double totalTakingMedicineRate) {
        this.breakfastTakingMedicineRate = breakfastTakingMedicineRate;
        this.lunchTakingMedicineRate = lunchTakingMedicineRate;
        this.dinnerTakingMedicineRate = dinnerTakingMedicineRate;
        this.totalTakingMedicineRate = totalTakingMedicineRate;
    }
}
