package com.remind.api.prescription.service;

import com.remind.api.connection.dto.reqeust.AcceptConnectionRequestDto;
import com.remind.api.prescription.dto.request.CreatePrescriptionRequestDto;
import com.remind.api.connection.dto.reqeust.RequestConnectionRequestDto;
import com.remind.api.connection.dto.response.AcceptConnectionResponseDto;
import com.remind.api.prescription.dto.response.CreatePrescriptionResponseDto;
import com.remind.api.connection.dto.response.RequestConnectionResponseDto;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.enums.MemberErrorCode;
import com.remind.core.domain.enums.PresciptionErrorCode;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.connection.enums.ConnectionStatus;
import com.remind.core.domain.prescription.repository.PrescriptionRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;

    private final MemberRepository memberRepository;

    /**
     * 의사,센터가 환자의 약 복용 정보를 업데이트하는 서비스 로직
     * @param userDetails
     * @param req
     * @return
     */
    @Transactional
    public CreatePrescriptionResponseDto createPrescription(UserDetailsImpl userDetails, CreatePrescriptionRequestDto req) {
        Member doctor = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Member patient = memberRepository.findById(req.memberId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 약 복용 정보는 의사 또는 센터만 등록 가능
        //밥먹고 수정
        if (!(doctor.getRolesType().equals(RolesType.ROLE_DOCTOR) || doctor.getRolesType().equals(RolesType.ROLE_CENTER))) {
            throw new PrescriptionException(PresciptionErrorCode.MEMBER_UNAUTHORIZED);
        }

        //일단 의사가 등록하는 로직, erd수정 후 추가 해야함.
        //Pending이면 못하게 해야함
        Prescription prescription = prescriptionRepository.findByDoctorIdAndPatientId(doctor.getId(), patient.getId())
                .orElseThrow(() -> new PrescriptionException(PresciptionErrorCode.PRESCRIPTION_NOT_FOUND));

        prescription.updatePrescriptionInfo(req.period(), req.prescriptionDate(), req.memo(), req.breakfastImportance(), req.lunchImportance(), req.dinnerImportance(), req.etcImportance());

        return CreatePrescriptionResponseDto.builder()
                .PrescriptionId(prescription.getId())
                .build();
    }
}
