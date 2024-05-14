package com.remind.api.prescription.service;

import com.remind.api.prescription.dto.request.RequestRelationRequestDto;
import com.remind.api.prescription.dto.response.RequestRelationResponseDto;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.enums.MemberErrorCode;
import com.remind.core.domain.enums.PresciptionErrorCode;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.prescription.enums.RelationsType;
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
     * 환자가 의사와의 관계를 요청하는 로직
     * @param req
     * @return
     */
    @Transactional
    public RequestRelationResponseDto requestRelation(UserDetailsImpl userDetails,RequestRelationRequestDto req) {
        Member patient = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Member doctor = memberRepository.findByMemberCode(req.doctorMemberCode())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        //환자가아니거나 역할이 없는경우 예외처리?
        //doctor가 아니거나 역할이 없는 경우 예외처리
        if (!doctor.getRolesType().equals(RolesType.ROLE_DOCTOR)) {
            throw new PrescriptionException(PresciptionErrorCode.MEMBER_NOT_DOCTOR);
        }

        //의사인경우, 테이블 추가 후 RelationType = Pending으로 설정
        Prescription prescription = Prescription.builder()
                .relationsType(RelationsType.PENDING)
                .patient(patient)
                .doctor(doctor)
                .build();

        prescriptionRepository.save(prescription);

        return RequestRelationResponseDto.builder()
                .PrescriptionId(prescription.getId())
                .build();
    }
}
