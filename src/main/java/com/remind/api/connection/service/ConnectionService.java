package com.remind.api.connection.service;

import com.remind.api.connection.dto.reqeust.AcceptConnectionRequestDto;
import com.remind.api.connection.dto.reqeust.RequestConnectionRequestDto;
import com.remind.api.connection.dto.response.AcceptConnectionResponseDto;
import com.remind.api.connection.dto.response.RequestConnectionResponseDto;
import com.remind.core.domain.common.exception.ConnectionException;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.connection.Connection;
import com.remind.core.domain.connection.enums.ConnectionStatus;
import com.remind.core.domain.connection.repository.ConnectionRepository;
import com.remind.core.domain.common.enums.ConnectionErrorCode;
import com.remind.core.domain.common.enums.MemberErrorCode;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final MemberRepository memberRepository;

    /**
     * 환자가 의사와의 관계를 요청하는 로직
     * @param req
     * @return
     */
    @Transactional
    public RequestConnectionResponseDto requestConnection(UserDetailsImpl userDetails, RequestConnectionRequestDto req) {
        Member patient = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        //의사 또는 센터
        Member targetMember = memberRepository.findByMemberCode(req.doctorMemberCode())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        //요청을 보내는 사람이 환자가아니거나 역할이 없는경우 예외처리?
        if (!patient.getRolesType().equals(RolesType.ROLE_PATIENT)) {
            throw new ConnectionException(ConnectionErrorCode.SEND_MEMBER_NOT_PATIENT);
        }

        //요청을 보내는 대상이 의사 또는 센터가 아니거나 역할이 없는 경우 예외처리
        if (!targetMember.getRolesType().equals(RolesType.ROLE_DOCTOR) &&
                !targetMember.getRolesType().equals(RolesType.ROLE_CENTER)) {
            throw new ConnectionException(ConnectionErrorCode.TARGET_MEMBER_NOT_DOCTOR_OR_CENTER);
        }

        //이미 보낸 요청이 존재할 경우 예외처리
        if (connectionRepository.findByTargetMemberIdAndPatientId(targetMember.getId(), patient.getId()).isPresent()) {
            throw new ConnectionException(ConnectionErrorCode.DUPLICATE_CONNECTION_REQUEST);
        }

        //테이블 추가 후 status = Pending으로 설정
        Connection connection = Connection.builder()
                .patient(patient)
                .targetMember(targetMember)
                .connectionStatus(ConnectionStatus.PENDING)
                .build();

        connectionRepository.save(connection);

        return RequestConnectionResponseDto.builder()
                .ConnectionId(connection.getId())
                .build();
    }

    /**
     * 의사 또는 센터가 환자와의 관계를 수락하는 로직
     * @param req
     * @return
     */
    @Transactional
    public AcceptConnectionResponseDto acceptConnection(UserDetailsImpl userDetails, AcceptConnectionRequestDto req) {
        Member targetMember = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Member patient = memberRepository.findById(req.memberId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        //수락 요청을 보내는 사람이 의사 또는 센터가 아니거나 역할이 없는 경우 예외처리
        if (!targetMember.getRolesType().equals(RolesType.ROLE_DOCTOR) &&
                !targetMember.getRolesType().equals(RolesType.ROLE_CENTER)) {
            throw new ConnectionException(ConnectionErrorCode.SEND_MEMBER_NOT_DOCTOR_OR_CENTER);
        }

        //수락 요청을 보낼 대상이 환자 아니거나 역할이 없는 경우 예외처리
        if (!patient.getRolesType().equals(RolesType.ROLE_PATIENT)) {
            throw new ConnectionException(ConnectionErrorCode.TARGET_MEMBER_NOT_PATIENT);
        }

        //Pending 상태의 요청이 없는 경우 예외처리
        Connection connection = connectionRepository.findByTargetMemberIdAndPatientId(targetMember.getId(), patient.getId())
                .orElseThrow(() -> new ConnectionException(ConnectionErrorCode.NO_CONNECTION_REQUEST));

        //이미 요청을 수락한경우 예외처리 // 위에랑 같은가...?
        if (connection.getConnectionStatus().equals(ConnectionStatus.ACCEPT)) {
            throw new ConnectionException(ConnectionErrorCode.ALREADY_CONNECTION_ACCEPTED);
        }

        //커넥션을 수락
        connection.updateConnectionStatus(ConnectionStatus.ACCEPT);


        return AcceptConnectionResponseDto.builder()
                .connectionId(connection.getId())
                .build();
    }

}
