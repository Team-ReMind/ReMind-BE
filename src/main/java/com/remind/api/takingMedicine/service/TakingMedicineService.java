package com.remind.api.takingMedicine.service;

import com.remind.api.takingMedicine.dto.request.TakingMedicineInfoRequest;
import com.remind.api.takingMedicine.dto.response.TakingMedicineInfoResponse;
import com.remind.core.domain.takingMedicine.repository.TakingMedicineRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TakingMedicineService {
    private final TakingMedicineRepository takingMedicineRepository;

    /**
     * 특정 날짜의 약 복용 정보를 조회하는 서비스 로직
     * @param userDetails
     * @param req
     * @return
     */
//    public TakingMedicineInfoResponse getTakingMedicineInfoResponse(UserDetailsImpl userDetails, TakingMedicineInfoRequest req, LocalDate date) {
//
//    }
}
