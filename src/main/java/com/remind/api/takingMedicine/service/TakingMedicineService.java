package com.remind.api.takingMedicine.service;

import com.remind.api.takingMedicine.dto.TakingMedicineDto;
import com.remind.api.takingMedicine.dto.request.CheckTakingMedicineRequest;
import com.remind.api.takingMedicine.dto.response.CheckTakingMedicineResponse;
import com.remind.api.takingMedicine.dto.response.TakingMedicineInfoResponse;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.common.exception.TakingMedicineException;
import com.remind.core.domain.enums.PresciptionErrorCode;
import com.remind.core.domain.enums.TakingMedicineErrorCode;
import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.prescription.repository.PrescriptionRepository;
import com.remind.core.domain.takingMedicine.TakingMedicine;
import com.remind.core.domain.takingMedicine.enums.MedicinesType;
import com.remind.core.domain.takingMedicine.repository.TakingMedicineRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TakingMedicineService {
    private final TakingMedicineRepository takingMedicineRepository;
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
     *
     * @param userDetails
     * @return
     */
    @Transactional(readOnly = true)
    public TakingMedicineInfoResponse getDailyTakingMedicineInfo(UserDetailsImpl userDetails, Long memberId, LocalDate date) {

        //특정 날짜, 환자의 약 복용리스트
        List<TakingMedicine> takingMedicineList = takingMedicineRepository.findAllByDateAndMemberId(date, memberId);
        for (TakingMedicine takingMedicine : takingMedicineList) {
            System.out.println(takingMedicine.getDate().toString() + takingMedicine.getMedicinesType() + "@@id : " + takingMedicine.getPrescription().getId());
        }

        List<TakingMedicineDto> takingMedicineDtos = new ArrayList<>();

        // 조회 결과 없음!
        if (takingMedicineList.isEmpty()) {
            return TakingMedicineInfoResponse.builder()
                    .takingMedicineDtos(takingMedicineDtos)
                    .build();
        }

        //처방 정보 가져오기
        Prescription prescription = prescriptionRepository.findById(takingMedicineList.get(0).getPrescription().getId())
                .orElseThrow(() -> new PrescriptionException(PresciptionErrorCode.PRESCRIPTION_NOT_FOUND));

        takingMedicineDtos = takingMedicineList.stream().map(takingMedicine -> {
            int importance = 999;
            switch (takingMedicine.getMedicinesType()) {
                case BREAKFAST:
                    importance = prescription.getBreakfastImportance();
                    break;
                case LUNCH:
                    importance = prescription.getLunchImportance();
                    break;
                case DINNER:
                    importance = prescription.getDinnerImportance();
                    break;
                default:
                    throw new PrescriptionException(PresciptionErrorCode.WRONG_MEDICINE_TYPE);
            }

            return TakingMedicineDto.builder()
                    .takingMedicineId(takingMedicine.getId())
                    .medicinesType(takingMedicine.getMedicinesType())
                    .isTaking(takingMedicine.getIsTaking())
                    .takingTime(takingMedicine.getTakingTime())
                    .notTakingReason(takingMedicine.getNotTakingReason())
                    .importance(importance)
                    .build();
        }).collect(Collectors.toList());

        return TakingMedicineInfoResponse.builder()
                .takingMedicineDtos(takingMedicineDtos)
                .build();

//
    }

    /**
     * 특정 날짜약 복용 정보를 등록하는 서비스 로직
     *
     * @return
     */
    @Transactional
    public CheckTakingMedicineResponse checkTakingMedicine(UserDetailsImpl userDetails,
                                                           CheckTakingMedicineRequest req,
                                                           LocalDate date,
                                                           MedicinesType medicinesType,
                                                           Boolean isTaking) {
        List<TakingMedicine> takingMedicineList = takingMedicineRepository.findAllByDateAndMemberIdAndMedicinesType(date, userDetails.getMemberId(), medicinesType);

        // 조회 결과 없음!
        if (takingMedicineList.isEmpty()) {
            throw new TakingMedicineException(TakingMedicineErrorCode.TAKING_MEDICINE_NOT_FOUND);
        }

        //현재는 약 복용 정보가 하나만 존재한다고 가정
        TakingMedicine takingMedicine = takingMedicineList.get(0);

        //약 복용
        if (isTaking) {
            takingMedicine.updateTakingTime(LocalTime.now());

        } else { //약 미복용
            takingMedicine.updateNotTakingReason(req.notTakingReason());


        }
        return CheckTakingMedicineResponse.builder()
                .notTakingReason(req.notTakingReason())
                .isTaking(isTaking)
                .build();

    }
}