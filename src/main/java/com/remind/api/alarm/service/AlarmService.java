package com.remind.api.alarm.service;

import static com.remind.core.domain.common.enums.AlarmErrorCode.*;
import static com.remind.core.domain.common.enums.PresciptionErrorCode.*;

import com.remind.api.alarm.dto.request.AlarmCreateRequestDto;
import com.remind.api.alarm.dto.response.AlarmListResponseDto;
import com.remind.core.domain.alarm.repository.AlarmListRepository;
import com.remind.core.domain.alarm.Alarm;
import com.remind.core.domain.alarm.AlarmDay;
import com.remind.core.domain.alarm.enums.AlarmDayOfWeek;
import com.remind.core.domain.alarm.repository.AlarmDayRepository;
import com.remind.core.domain.alarm.repository.AlarmRepository;
import com.remind.core.domain.common.enums.MemberErrorCode;
import com.remind.core.domain.common.exception.AlarmException;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.PrescriptionException;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.prescription.Prescription;
import com.remind.core.domain.prescription.repository.PrescriptionRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AlarmDayRepository alarmDayRepository;
    private final AlarmListRepository alarmListRepository;
    private final MemberRepository memberRepository;

    /**
     * 처방전에 대해 알림 생성
     */
    @Transactional
    public Long createAlarm(UserDetailsImpl userDetails, AlarmCreateRequestDto dto) {

        // 요청에 EVERYDAY가 포함된 경우, 다른 요일과 함께 요청 불가
        if (dto.alarmDaysOfWeek().contains(AlarmDayOfWeek.EVERYDAY) && dto.alarmDaysOfWeek().size() > 1) {
            throw new AlarmException(INVALID_CREATE_REQUEST);
        }

        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Prescription prescription = prescriptionRepository.findById(dto.prescriptionId())
                .orElseThrow(() -> new PrescriptionException(
                        PRESCRIPTION_NOT_FOUND));

        Alarm alarm = alarmRepository.save(
                Alarm.builder()
                        .prescription(prescription)
                        .alarmTime(LocalTime.of(Integer.parseInt(dto.alarmHour()), Integer.parseInt(dto.alarmMinute())))
                        .fcmToken(member.getFcmToken())
                        .build());

        dto.alarmDaysOfWeek().forEach((dayOfWeek) -> {
            alarmDayRepository.save(
                    AlarmDay.builder()
                            .alarm(alarm)
                            .alarmDay(dayOfWeek)
                            .build()
            );
        });

        return alarm.getId();
    }

    /**
     * 알림 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AlarmListResponseDto> getAlarms(UserDetailsImpl userDetails, Long prescriptionId) {

        List<AlarmListResponseDto> response = new ArrayList<>();
        List<Alarm> alarms = alarmRepository.findAllByPrescriptionId(prescriptionId);

        alarms.forEach(alarm -> {
            // Alarm entity와 연관된 AlarmDay entity 조회
            List<AlarmDayOfWeek> dayOfWeeks = alarmDayRepository.findAllByAlarmId(alarm.getId()).stream()
                    .map(AlarmDay::getAlarmDay)
                    .collect(Collectors.toList());

            response.add(new AlarmListResponseDto(alarm.getId(), alarm.getAlarmTime().toString(), dayOfWeeks));
        });

        return response;
    }
}
