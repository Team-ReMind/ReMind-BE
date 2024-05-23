package com.remind.api.takingMedicine.service;

import com.remind.api.member.service.PatientService;
import com.remind.api.takingMedicine.dto.DailyTakingMedicineDto;
import com.remind.api.takingMedicine.dto.MonthlyTakingMedicineDto;
import com.remind.api.takingMedicine.dto.request.CreateTakingMedicineRequest;
import com.remind.api.takingMedicine.dto.response.CreateTakingMedicineResponse;
import com.remind.api.takingMedicine.dto.response.DailyTakingMedicineInfoResponse;
import com.remind.api.takingMedicine.dto.response.MonthlyTakingMedicineInfoResponse;
import com.remind.api.takingMedicine.dto.response.TakingMedicineRateResponse;
import com.remind.core.domain.common.enums.MemberErrorCode;
import com.remind.core.domain.common.enums.TakingMedicineErrorCode;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.TakingMedicineException;
import com.remind.core.domain.member.Patient;
import com.remind.core.domain.member.repository.PatientRepository;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TakingMedicineService {
    private final TakingMedicineRepository takingMedicineRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PatientService patientService;
    private final PatientRepository patientRepository;


    /**
     * 특정 날짜의 특정 유저의 약 복용 정보를 조회하는 서비스 로직
     * @param userDetails
     * @return
     */
    @Transactional(readOnly = true)
    public DailyTakingMedicineInfoResponse getDailyTakingMedicineInfo(UserDetailsImpl userDetails, Long memberId, LocalDate date) {

        //dto에 포함될 객체
        List<DailyTakingMedicineDto> dailyTakingMedicineDtos = new ArrayList<>();

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
            return DailyTakingMedicineInfoResponse.builder()
                    .dailyTakingMedicineDtos(dailyTakingMedicineDtos)
                    .build();
        }


        //오늘 필요한 약 처방 정보
        Prescription prescription = optionalPrescription.get();


        //오늘 복용 또는 미복용했던 정보(아점저)
        List<TakingMedicine> takingMedicineList = takingMedicineRepository.findAllByPrescriptionIdAndDate(prescription.getId(), date);

        //아침 약 정보
        DailyTakingMedicineDto breakfastDto = takingMedicineList.stream()
                .filter(takingMedicine -> takingMedicine.getMedicinesType().equals(MedicinesType.BREAKFAST))
                .findFirst()
                .map(takingMedicine -> {
                    //약을 복용한 정보가 존재할 경우 - 복용
                    if (takingMedicine.getIsTaking() == true) {
                        return DailyTakingMedicineDto
                                .ofTaking(prescription.getId(), MedicinesType.BREAKFAST, prescription.getBreakfastImportance(), takingMedicine.getTakingTime());
                    }
                    //약을 복용한 정보가 존재할 경우 - 미복용
                    else {
                        return DailyTakingMedicineDto
                                .ofUntaking(prescription.getId(), MedicinesType.BREAKFAST, prescription.getBreakfastImportance(), takingMedicine.getNotTakingReason());
                    }
                    //약을 복용 또는 미복용 정보를 체크하지 않은 경우(미복용)
                })
                .orElse(DailyTakingMedicineDto
                        .ofUnchecking(prescription.getId(), MedicinesType.BREAKFAST, prescription.getBreakfastImportance()));

        //점심 약 정보
        DailyTakingMedicineDto lunchDto = takingMedicineList.stream()
                .filter(takingMedicine -> takingMedicine.getMedicinesType().equals(MedicinesType.LUNCH))
                .findFirst()
                .map(takingMedicine -> {
                    //약을 복용한 정보가 존재할 경우 - 복용
                    if (takingMedicine.getIsTaking() == true) {
                        return DailyTakingMedicineDto
                                .ofTaking(prescription.getId(), MedicinesType.LUNCH, prescription.getLunchImportance(), takingMedicine.getTakingTime());
                    }
                    //약을 복용한 정보가 존재할 경우 - 미복용
                    else {
                        return DailyTakingMedicineDto
                                .ofUntaking(prescription.getId(), MedicinesType.LUNCH, prescription.getLunchImportance(), takingMedicine.getNotTakingReason());
                    }
                    //약을 복용 또는 미복용 정보를 체크하지 않은 경우(미복용)
                })
                .orElse(DailyTakingMedicineDto
                        .ofUnchecking(prescription.getId(), MedicinesType.LUNCH, prescription.getLunchImportance()));

        //저녁 약 정보
        DailyTakingMedicineDto dinnerDto = takingMedicineList.stream()
                .filter(takingMedicine -> takingMedicine.getMedicinesType().equals(MedicinesType.DINNER))
                .findFirst()
                .map(takingMedicine -> {
                    //약을 복용한 정보가 존재할 경우 - 복용
                    if (takingMedicine.getIsTaking() == true) {
                        return DailyTakingMedicineDto
                                .ofTaking(prescription.getId(), MedicinesType.DINNER, prescription.getDinnerImportance(), takingMedicine.getTakingTime());
                    }
                    //약을 복용한 정보가 존재할 경우 - 미복용
                    else {
                        return DailyTakingMedicineDto
                                .ofUntaking(prescription.getId(), MedicinesType.DINNER, prescription.getDinnerImportance(), takingMedicine.getNotTakingReason());
                    }
                    //약을 복용 또는 미복용 정보를 체크하지 않은 경우(미복용)
                })
                .orElse(DailyTakingMedicineDto
                        .ofUnchecking(prescription.getId(), MedicinesType.DINNER, prescription.getDinnerImportance()));

        //오늘(아침,점심,저녁) 먹어야 할 약 정보 dto - 중요도, 복용여부, 복용시간 등

        dailyTakingMedicineDtos.add(breakfastDto);
        dailyTakingMedicineDtos.add(lunchDto);
        dailyTakingMedicineDtos.add(dinnerDto);


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
        //파라미터가 0이면, 나를 조회하도록 하기.
        if (memberId == 0) {
            memberId = userDetails.getMemberId();
        }

        //멤버가 가진 모든 처방 정보 조회
        List<Prescription> prescriptionList = prescriptionRepository.findAllByPatientId(memberId);

        //1일부터 말일까지
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        //한 달의 정보를 담을 dto
        List<MonthlyTakingMedicineDto> monthlyTakingMedicineDtos = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // 날짜가 약복용이 필요했던 날인 경우
            LocalDate tmpDate = date;
            Optional<Prescription> optionalPrescription = prescriptionList.stream().filter(tmpPrescription -> tmpPrescription.isDateInPrescription(tmpDate))
                    .findFirst();

            // 약 복용이 필요없는 날
            if (optionalPrescription.isEmpty()) {
                monthlyTakingMedicineDtos.add(MonthlyTakingMedicineDto.builder()
                        .needMedicine(false)
                        .date(date.getDayOfMonth())
                        .build());
                continue;
            }

            //약 복용이 필요한 날. 해당되는 약 처방 정보
            Prescription prescription = optionalPrescription.get();

            int realTakingMedicineCount = takingMedicineRepository.countByDateAndPrescriptionIdAndIsTakingIsTrue(date, prescription.getId());
            int needTakingMedicineCount = prescription.takingMedicineCount();
            int takingMedicineLevel;

            //약 복용 정도
            if(realTakingMedicineCount == needTakingMedicineCount){
                takingMedicineLevel = 2;
            } else if (realTakingMedicineCount == 0) {
                takingMedicineLevel = 0;
            } else {
                takingMedicineLevel = 1;
            }

            monthlyTakingMedicineDtos.add(MonthlyTakingMedicineDto.builder()
                    .needMedicine(true)
                    .date(date.getDayOfMonth())
                    .takingCount(realTakingMedicineCount)
                    .takingLevel(takingMedicineLevel)
                    .build());
        }

        return MonthlyTakingMedicineInfoResponse.builder()
                .monthlyTakingMedicineDtos(monthlyTakingMedicineDtos)
                .build();

    }


    /**
     * 특정 날짜약 복용 정보를 등록하는 서비스 로직
     *
     * @return
     */
    @Transactional
    public CreateTakingMedicineResponse createTakingMedicine(UserDetailsImpl userDetails,
                                                            CreateTakingMedicineRequest req) {

        //멤버가 가진 처방 중에서, 오늘 날짜가 포함된 처방을 찾기
        Prescription prescription = prescriptionRepository.findByPatientIdAndValidDate(userDetails.getMemberId(), LocalDate.now())
                .orElseThrow(() -> new TakingMedicineException(TakingMedicineErrorCode.TAKING_MEDICINE_NOT_FOUND));

        //이미 복용정보가 있으면 X
        takingMedicineRepository.findByPrescriptionIdAndDateAndMedicinesType(prescription.getId(), LocalDate.now(), req.medicinesType())
                .ifPresent((a) -> {
                    throw new TakingMedicineException(TakingMedicineErrorCode.TAKING_MEDICINE_ALREADY_EXIST);
                });
        //중요도 0이면 복용 못하게 해야함
        if ((req.medicinesType() == MedicinesType.BREAKFAST && prescription.getBreakfastImportance() == 0) ||
                (req.medicinesType() == MedicinesType.LUNCH && prescription.getLunchImportance() == 0) ||
                (req.medicinesType() == MedicinesType.DINNER && prescription.getDinnerImportance() == 0)) {
            throw new TakingMedicineException(TakingMedicineErrorCode.DONT_NEED_TAKING_MEDICINE);
        }

        takingMedicineRepository.save(
                TakingMedicine.builder()
                        .prescription(prescription)
                        .date(LocalDate.now())
                        .medicinesType(req.medicinesType())
                        .isTaking(req.isTaking())
                        .takingTime(LocalTime.now())
                        .notTakingReason(req.notTakingReason())
                        .build());

        //patient와 에 대해 약 복용률 업데이트
        //하루한번>??
        patientService.updateTakingMedicineRate(userDetails.getMemberId(), prescription.getId());

        return CreateTakingMedicineResponse.builder()
                .isTaking(req.isTaking())
                .notTakingReason(req.notTakingReason())
                .build();

    }

    //약 복용률을 조회하는 pai
    @Transactional(readOnly = true)
    public TakingMedicineRateResponse getTakingMedicineRate(UserDetailsImpl userDetails,
                                                            Long memberId) {
        //파라미터가 0이면, 나를 조회하도록 하기.
        if (memberId == 0) {
            memberId = userDetails.getMemberId();
        }

        Patient patient = patientRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return TakingMedicineRateResponse.builder()
                .breakfastRate(patient.getBreakfastTakingMedicineRate()*100)
                .lunchRate(patient.getLunchTakingMedicineRate()*100)
                .dinnerRate(patient.getDinnerTakingMedicineRate()*100)
                .totalRate(patient.getTotalTakingMedicineRate()*100)
                .build();
    }
}