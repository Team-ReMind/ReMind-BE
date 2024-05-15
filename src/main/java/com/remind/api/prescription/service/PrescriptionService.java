package com.remind.api.prescription.service;

import com.remind.api.prescription.dto.PrescriptionDto;
import com.remind.api.prescription.dto.request.CreatePrescriptionRequestDto;
import com.remind.api.prescription.dto.response.CreatePrescriptionResponseDto;
import com.remind.api.prescription.dto.response.PrescriptionInfoResponseDto;
import com.remind.api.takingMedicine.service.TakingMedicineService;
import com.remind.core.domain.common.exception.ConnectionException;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.connection.Connection;
import com.remind.core.domain.connection.repository.ConnectionRepository;
import com.remind.core.domain.common.enums.ConnectionErrorCode;
import com.remind.core.domain.common.enums.MemberErrorCode;
import com.remind.core.domain.common.enums.PresciptionErrorCode;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.prescription.repository.PrescriptionRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final ConnectionRepository connectionRepository;
    private final MemberRepository memberRepository;
    private final TakingMedicineService takingMedicineService;

    /**
     * 의사가 환자의 약 복용 정보를 업데이트하는 서비스 로직
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


        // 약 복용 정보는 의사만 등록 가능
        if (!doctor.getRolesType().equals(RolesType.ROLE_DOCTOR)) {
            throw new PrescriptionException(PresciptionErrorCode.PRESCRIPTION_ONLY_DOCTOR);
        }

        // 약 복용 정보는 환자에게만
        if (!patient.getRolesType().equals(RolesType.ROLE_PATIENT)) {
            throw new PrescriptionException(PresciptionErrorCode.PRESCRIPTION_ONLY_TO_PATIENT);
        }

        //의사-환자 커넥션이 없는 경우
        Connection connection = connectionRepository.findByTargetMemberIdAndPatientId(doctor.getId(), patient.getId())
                .orElseThrow(() -> new ConnectionException(ConnectionErrorCode.NO_CONNECTION_REQUEST));


        //prescription정보가 존재하지 않으면, 생성하기
        Prescription prescription = prescriptionRepository.findByConnectionId(connection.getId())
                .orElseGet(() -> registerPrescription(connection));

        //업데이트
        prescription.updatePrescriptionInfo(req.period(), req.memo(), req.breakfastImportance(), req.lunchImportance(), req.dinnerImportance(), req.etcImportance());

        //해당 날짜의 약 복용 엔티티 생성
        takingMedicineService.updateTakingMedicine(prescription.getId(),prescription.getPrescriptionDate(),prescription.getPeriod());

        return CreatePrescriptionResponseDto.builder()
                .PrescriptionId(prescription.getId())
                .build();
    }

    private Prescription registerPrescription(Connection connection) {
        return prescriptionRepository.save(Prescription.builder()
                .prescriptionDate(LocalDate.now())
                .connection(connection)
                .build());
    }

    /**
     * 특정 멤버의 처방 정보를 조회하는 로직
     * @param
     * @return
     */
    @Transactional(readOnly = true)
    public PrescriptionInfoResponseDto getPrescriptionInfo(UserDetailsImpl userDetails, Long memberId) {
        //아무나 조회할 수 없도록 수정해야 함
        Member patient = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<Prescription> patientPrescriptionList = prescriptionRepository.findAllByPatientId(patient.getId());

        List<PrescriptionDto> prescriptionDtos = new ArrayList<>();
        prescriptionDtos = patientPrescriptionList.stream()
                .map(prescription -> PrescriptionDto.builder()
                        .prescriptionId(prescription.getId())
                        .build())
                .collect(Collectors.toList());


        System.out.println("sze : " +prescriptionDtos.size());
        return PrescriptionInfoResponseDto.builder()
                .prescriptionDtos(prescriptionDtos)
                .build();

    }
}
