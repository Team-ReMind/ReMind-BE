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
//
//    /**
//     * 환자가 의사와의 관계를 요청하는 로직
//     *
//     * @param req
//     * @return
//     */
//    @Transactional
//    public RequestConnectionResponseDto requestRelation(UserDetailsImpl userDetails, RequestConnectionRequestDto req) {
//        Member patient = memberRepository.findById(userDetails.getMemberId())
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        Member doctor = memberRepository.findByMemberCode(req.doctorMemberCode())
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        //요청을 보내는 사람이 환자가아니거나 역할이 없는경우 예외처리?
//        if (!patient.getRolesType().equals(RolesType.ROLE_USER)) {
//            throw new PrescriptionException(PresciptionErrorCode.MEMBER_NOT_PATIENT);
//        }
//
//        //요청을 보내는 대상이 의사가 아니거나 역할이 없는 경우 예외처리
//        if (!doctor.getRolesType().equals(RolesType.ROLE_DOCTOR)) {
//            throw new PrescriptionException(PresciptionErrorCode.MEMBER_NOT_DOCTOR);
//        }
//
//        //이미 보낸 요청이 존재할 경우 예외처리
//        if (con.findByDoctorIdAndPatientId(doctor.getId(), patient.getId()).isPresent()) {
//            throw new PrescriptionException(PresciptionErrorCode.DUPLICATE_PRESCRIPTION_REQUEST);
//        }
//
//        if (prescriptionRepository.findByDoctorIdAndPatientId(doctor.getId(), patient.getId()).isPresent()) {
//            throw new PrescriptionException(PresciptionErrorCode.DUPLICATE_PRESCRIPTION_REQUEST);
//        }
//
//        //의사인경우, 테이블 추가 후 RelationType = Pending으로 설정
//        Prescription prescription = Prescription.builder()
//                .connectionStatus(ConnectionStatus.PENDING)
//                .patient(patient)
//                .doctor(doctor)
//                .build();
//
//        prescriptionRepository.save(prescription);
//
//        return RequestConnectionResponseDto.builder()
//                .PrescriptionId(prescription.getId())
//                .build();
//    }
//
//    /**
//     * 의사가 환자와의 관계를 수락하는 로직
//     *
//     * @param req
//     * @return
//     */
//    @Transactional
//    public AcceptConnectionResponseDto acceptRelation(UserDetailsImpl userDetails, AcceptConnectionRequestDto req) {
//        Member doctor = memberRepository.findById(userDetails.getMemberId())
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        Member patient = memberRepository.findById(req.memberId())
//                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        //요청을 보내는 사람이 의사가 아니거나 역할이 없는 경우 예외처리
//        if (!doctor.getRolesType().equals(RolesType.ROLE_DOCTOR)) {
//            throw new PrescriptionException(PresciptionErrorCode.MEMBER_NOT_DOCTOR);
//        }
//
//        //요청을 보내는 대상이 환자 아니거나 역할이 없는 경우 예외처리
//        if (!patient.getRolesType().equals(RolesType.ROLE_USER)) {
//            throw new PrescriptionException(PresciptionErrorCode.MEMBER_NOT_PATIENT);
//        }
//
//        //Pending 상태의 요청이 없는 경우 예외처리
//        Prescription prescription = prescriptionRepository.findByDoctorIdAndPatientId(doctor.getId(), patient.getId())
//                .orElseThrow(() -> new PrescriptionException(PresciptionErrorCode.NO_PRESCRIPTION_REQUEST));
//        if (prescription.getConnectionStatus().equals(ConnectionStatus.ACCEPT)) {
//            throw new PrescriptionException(PresciptionErrorCode.ALREADY_PRESCRIPTION_ACCEPTED);
//        }
//
//        prescription.updateRelationsType(ConnectionStatus.ACCEPT);
//
//
//        return AcceptConnectionResponseDto.builder()
//                .prescriptionId(prescription.getId())
//                .build();
//    }

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
