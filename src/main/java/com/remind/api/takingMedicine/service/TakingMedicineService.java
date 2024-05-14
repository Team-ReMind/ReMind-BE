package com.remind.api.takingMedicine.service;

import com.remind.api.prescription.dto.PrescriptionDto;
import com.remind.api.prescription.dto.response.PrescriptionInfoResponseDto;
import com.remind.api.prescription.service.PrescriptionService;
import com.remind.api.takingMedicine.dto.request.TakingMedicineInfoRequest;
import com.remind.api.takingMedicine.dto.response.TakingMedicineInfoResponse;
import com.remind.api.takingMedicine.dto.response.UpdateTakingMedicineResponseDto;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.enums.PresciptionErrorCode;
import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.prescription.repository.PrescriptionRepository;
import com.remind.core.domain.takingMedicine.TakingMedicine;
import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import com.remind.core.domain.takingMedicine.repository.TakingMedicineRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TakingMedicineService {
    private final TakingMedicineRepository takingMedicineRepository;
//    private final PrescriptionService prescriptionService;
    private final PrescriptionRepository prescriptionRepository;

    public void updateTakingMedicine(Long prescriptionId, LocalDate prescriptionDate, int period) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new PrescriptionException(PresciptionErrorCode.PRESCRIPTION_NOT_FOUND));

        LocalDate endDate = prescriptionDate.plus(period, ChronoUnit.DAYS);

        //처방 날짜부터 마지막 날
        List<LocalDate> validDates = prescriptionDate.datesUntil(endDate.plusDays(0)).collect(Collectors.toList());
        System.out.println("prescriptionDate : " + prescriptionDate + "period : " + period);
        validDates.forEach(System.out::println);
        System.out.println("==========");

        // 기존 약 복용 데이터 가져오기
        List<TakingMedicine> existingTakingMedicineList = takingMedicineRepository.findAllByPrescriptionId(prescriptionId);

        // 유효하지 않은 날짜의 데이터 삭제
        for (TakingMedicine tm : existingTakingMedicineList) {
            if (!validDates.contains(tm.getDate())) {
                takingMedicineRepository.delete(tm);
            }
        }

        // 유효한 날짜에 대해 약 복용 데이터 생성 및 저장
        for (LocalDate date : validDates) {
            if (existingTakingMedicineList.stream().noneMatch(tm -> tm.getDate().equals(date))) {
                createAndSaveTakingMedicine(prescription, date, MedicinesType.BREAKFAST);
                createAndSaveTakingMedicine(prescription, date, MedicinesType.LUNCH);
                createAndSaveTakingMedicine(prescription, date, MedicinesType.DINNER);
            }
        }
    }
    private void createAndSaveTakingMedicine(Prescription prescription, LocalDate date, MedicinesType medicinesType) {
        TakingMedicine takingMedicine = TakingMedicine.builder()
                .prescription(prescription)
                .date(date)
                .medicinesType(medicinesType)
                .isTaking(false)
                .build();
        takingMedicineRepository.save(takingMedicine);
    }

    /**
     * 특정 날짜의 특정 유저의 약 복용 정보를 조회하는 서비스 로직
     * @param userDetails
     * @param req
     * @return
     */
//    public TakingMedicineInfoResponse getTakingMedicineInfoResponse(UserDetailsImpl userDetails, TakingMedicineInfoRequest req, Long memberId, LocalDate date) {
//        //특정 환자의 처방 목록
//        List<Prescription> patientPrescriptionList = prescriptionRepository.findAllByPatientId(memberId);
//
//
//
//        for (Prescription prescription : patientPrescriptionList) {
//            LocalDate prescriptionDate = prescription.getPrescriptionDate();
//            LocalDate endDate = prescriptionDate.plus(10, ChronoUnit.DAYS);
//            //date가 처방 날짜 범위 내에 존재하는 경우
//            if (!date.isBefore(prescriptionDate) && !date.isAfter(endDate)) {
//
//            }
//        }
//    }
}
