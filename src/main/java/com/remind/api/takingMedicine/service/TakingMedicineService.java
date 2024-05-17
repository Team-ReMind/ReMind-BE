package com.remind.api.takingMedicine.service;

import com.remind.api.takingMedicine.dto.DailyTakingMedicineDto;
import com.remind.api.takingMedicine.dto.MonthlyTakingMedicineDto;
import com.remind.api.takingMedicine.dto.request.CheckTakingMedicineRequest;
import com.remind.api.takingMedicine.dto.response.CheckTakingMedicineResponse;
import com.remind.api.takingMedicine.dto.response.DailyTakingMedicineInfoResponse;
import com.remind.api.takingMedicine.dto.response.MonthlyTakingMedicineInfoResponse;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.common.exception.TakingMedicineException;
import com.remind.core.domain.common.enums.PresciptionErrorCode;
import com.remind.core.domain.common.enums.TakingMedicineErrorCode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
     * @param userDetails
     * @return
     */
    @Transactional(readOnly = true)
    public DailyTakingMedicineInfoResponse getDailyTakingMedicineInfo(UserDetailsImpl userDetails, Long memberId, LocalDate date) {

        //파라미터가 0이면, 나를 조회하도록 하기.
        if (memberId == 0) {
            memberId = userDetails.getMemberId();
        }

        //멤버가 가진 모든 처방 정보 조회
        List<Prescription> prescriptionList = prescriptionRepository.findAllByPatientId(memberId);

        // 날짜가 약복용이 필요한 날인 경우
        Optional<Prescription> optionalPrescription = prescriptionList.stream().filter(tmpPrescription -> tmpPrescription.isDateInPrescription(date))
                .findFirst();

        //등록된 약이 없음!
        if (optionalPrescription.isEmpty()) {
            return null;
        }

        //오늘 필요한 약 처방 정보
        Prescription prescription = optionalPrescription.get();

        //기본적으로 반환할 dto.
        AtomicReference<DailyTakingMedicineDto> breakfastDto = new AtomicReference<>(DailyTakingMedicineDto.builder()
                .medicinesType(MedicinesType.BREAKFAST)
                .importance(prescription.getBreakfastImportance())
                .build());
        AtomicReference<DailyTakingMedicineDto> lunchDto = new AtomicReference<>(DailyTakingMedicineDto.builder()
                .medicinesType(MedicinesType.LUNCH)
                .importance(prescription.getLunchImportance())
                .build());
        AtomicReference<DailyTakingMedicineDto> dinnerDto = new AtomicReference<>(DailyTakingMedicineDto.builder()
                .medicinesType(MedicinesType.DINNER)
                .importance(prescription.getDinnerImportance())
                .build());


        //오늘 먹어야 할 약 정보
        List<TakingMedicine> takingMedicineList = takingMedicineRepository.findAllByPrescriptionIdAndDate(prescription.getId(), date);

        takingMedicineList.forEach(takingMedicine -> {
            //특정 시간의 약 복용/미복용 정보 존재
            switch (takingMedicine.getMedicinesType()) {
                case BREAKFAST :
                    breakfastDto.set(DailyTakingMedicineDto.builder()
                            .medicinesType(MedicinesType.BREAKFAST)
                            .importance(prescription.getBreakfastImportance())
                            .isTaking(takingMedicine.getIsTaking())
                            .takingTime(takingMedicine.getTakingTime())
                            .notTakingReason(takingMedicine.getNotTakingReason())
                            .build());
                    break;
                case LUNCH:
                    lunchDto.set(DailyTakingMedicineDto.builder()
                            .medicinesType(MedicinesType.LUNCH)
                            .importance(prescription.getLunchImportance())
                            .isTaking(takingMedicine.getIsTaking())
                            .takingTime(takingMedicine.getTakingTime())
                            .notTakingReason(takingMedicine.getNotTakingReason())
                            .build());
                    break;
                case DINNER:
                    dinnerDto.set(DailyTakingMedicineDto.builder()
                            .medicinesType(MedicinesType.DINNER)
                            .importance(prescription.getDinnerImportance())
                            .isTaking(takingMedicine.getIsTaking())
                            .takingTime(takingMedicine.getTakingTime())
                            .notTakingReason(takingMedicine.getNotTakingReason())
                            .build());
                    break;
                default:
                    throw new PrescriptionException(PresciptionErrorCode.WRONG_MEDICINE_TYPE);
            }
        });

        //오늘(아침,점심,저녁) 먹어야 할 약 정보 dto - 중요도, 복용여부, 복용시간 등
        List<DailyTakingMedicineDto> dailyTakingMedicineDtos = new ArrayList<>();
        dailyTakingMedicineDtos.add(breakfastDto.get());
        dailyTakingMedicineDtos.add(lunchDto.get());
        dailyTakingMedicineDtos.add(dinnerDto.get());


        return DailyTakingMedicineInfoResponse.builder()
                .dailyTakingMedicineDtos(dailyTakingMedicineDtos)
                .build();



    }

    /**
     * 특정 년,월 특정 유저의 약 복용 정보를 조회하는 서비스 로직
     *
     * @param userDetails
     * @return
     */
    @Transactional(readOnly = true)
    public MonthlyTakingMedicineInfoResponse getMonthlyTakingMedicineInfo(UserDetailsImpl userDetails,
                                                                          Long memberId,
                                                                          int year,
                                                                          int month) {
        //특정 날짜, 환자의 약 복용리스트
        List<TakingMedicine> takingMedicineList = takingMedicineRepository.findAllByYearAndMonthAndMemberId(memberId, year, month);


        List<MonthlyTakingMedicineDto> monthlyTakingMedicineDtos = new ArrayList<>();

        // 조회 결과 없음!
        if (takingMedicineList.isEmpty()) {
            return MonthlyTakingMedicineInfoResponse.builder()
                    .monthlyTakingMedicineDtos(monthlyTakingMedicineDtos)
                    .build();
        }

        //날짜에 따른 카운트를 저장하기 위한 해시맵
        HashMap<LocalDate, Integer> localDateCountHashMap = new HashMap<>();

        //아침,점심,저녁에 따른 카운트를 저장하기 위한 해시맵
        HashMap<MedicinesType, Integer> medicinesTypeCountHashMap = new HashMap<>();

        //복용 정보를 조회하여 날짜별, 아점저별로 카운트를 추가함
        takingMedicineList.forEach(takingMedicine -> {
            LocalDate date = takingMedicine.getDate();
            MedicinesType medicinesType = takingMedicine.getMedicinesType();

            Integer medicineTypeCount = medicinesTypeCountHashMap.getOrDefault(medicinesType, 0);
            Integer dateCount = localDateCountHashMap.getOrDefault(date, 0);

            //약을 복용했으면 카운트 추가
            if (takingMedicine.getIsTaking()) {
                medicinesTypeCountHashMap.put(medicinesType, medicineTypeCount + 1);
                localDateCountHashMap.put(date, dateCount + 1);
            }
            else{
                //약을 복용하지 않았어도, 해시맵에 추가
                medicinesTypeCountHashMap.put(medicinesType, medicineTypeCount);
                localDateCountHashMap.put(date, dateCount);
            }
        });

        //dto 변환하기 - 날 별
        monthlyTakingMedicineDtos = localDateCountHashMap.entrySet().stream().map(localDateIntegerEntry -> {
            MonthlyTakingMedicineDto monthlyTakingMedicineDto = MonthlyTakingMedicineDto.builder()
                    .date(localDateIntegerEntry.getKey())
                    .takingCount(localDateIntegerEntry.getValue())
                    .build();
            return monthlyTakingMedicineDto;
        }).collect(Collectors.toList());

        //dto변환하기 - 아점저 별
        int totalSize = localDateCountHashMap.size();
        int totalCount = totalSize * 3;

        int breakfastCount = medicinesTypeCountHashMap.getOrDefault(MedicinesType.BREAKFAST,0);
        int lunchCount = medicinesTypeCountHashMap.getOrDefault(MedicinesType.LUNCH,0);
        int dinnerCount = medicinesTypeCountHashMap.getOrDefault(MedicinesType.DINNER,0);

        System.out.println("totalSize = " + totalSize);
        System.out.println("totalCount = " + totalCount);
        System.out.println("breakfastCount = " + breakfastCount);
        System.out.println("lunchCount = " + lunchCount);
        System.out.println("dinnerCount = " + dinnerCount);

        double breakfastRatio = (double) breakfastCount / totalSize * 100;
        double lunchRatio = (double) lunchCount / totalSize * 100;
        double dinnerRatio =(double) dinnerCount / totalSize * 100;
        double totalRatio = (double) (breakfastCount + lunchCount + dinnerCount) / totalCount * 100;

        return MonthlyTakingMedicineInfoResponse.builder()
                .monthlyTakingMedicineDtos(monthlyTakingMedicineDtos)
                .breakfastRatio(breakfastRatio)
                .lunchRatio(lunchRatio)
                .dinnerRatio(dinnerRatio)
                .totalRatio(totalRatio)
                .build();
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