package com.remind.api.member.service;

import com.remind.core.domain.common.enums.MemberErrorCode;
import com.remind.core.domain.common.enums.PresciptionErrorCode;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.Patient;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.member.repository.PatientRepository;
import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.prescription.repository.PrescriptionRepository;
import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import com.remind.core.domain.takingMedicine.repository.TakingMedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final MemberRepository memberRepository;
    private final TakingMedicineRepository takingMedicineRepository;

    /**
     * 환자id와 처방id로 복용률을 업데이트 하는 메서드
     * @param patientId
     * @param prescriptionId
     */
    public void updateTakingMedicineRate(Long patientId, Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new PrescriptionException(PresciptionErrorCode.PRESCRIPTION_NOT_FOUND));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member member = memberRepository.findById(patientId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 아침, 점심, 저녁 약 실제 복용 횟수
        int realBreakfastCount = takingMedicineRepository.countByPrescriptionIdAndMedicinesType(prescriptionId, MedicinesType.BREAKFAST);
        int realLunchCount = takingMedicineRepository.countByPrescriptionIdAndMedicinesType(prescriptionId, MedicinesType.LUNCH);
        int realDinnerCount = takingMedicineRepository.countByPrescriptionIdAndMedicinesType(prescriptionId, MedicinesType.ETC);
        int realCount = realBreakfastCount + realLunchCount + realDinnerCount;

        // 아침, 점심, 저녁에 먹어야하는 복용 횟수
        int totalBreakfastCount = prescription.getBreakfastImportance() == 0 ? 0 : (prescription.getPeriod() + 1);
        int totalLunchCount = prescription.getLunchImportance() == 0 ? 0 : (prescription.getPeriod() + 1);
        int totalDinnerCount = prescription.getDinnerImportance() == 0 ? 0 : (prescription.getPeriod() + 1);
        int totalCount = totalBreakfastCount + totalLunchCount + totalDinnerCount;


        Double breakfastRate = totalBreakfastCount == 0 ? 0 : (double)realBreakfastCount / totalBreakfastCount;
        Double lunchRate = totalLunchCount == 0 ? 0 : (double)realLunchCount / totalLunchCount;
        Double dinnerRate = totalDinnerCount == 0 ? 0 : (double) realDinnerCount / totalDinnerCount;
        Double totalRate = totalCount == 0 ? 0 : (double)realCount / totalCount;


        patient.updateTakingMedicineRate(breakfastRate, lunchRate, dinnerRate, totalRate);
    }
}
