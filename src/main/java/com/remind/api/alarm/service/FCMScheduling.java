package com.remind.api.alarm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.remind.api.alarm.repository.AlarmListRepository;
import com.remind.api.alarm.util.fcm.FCMUtil;
import com.remind.core.domain.alarm.enums.AlarmDayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMScheduling {

    private final FCMUtil fcmUtil;
    private final AlarmListRepository alarmListRepository;

    private static Integer INTERVALMINUTE = 1;

    /**
     * 1분마다 스케줄링 작업 실행 >> 해당 분에 존재하는 모든 알림에 대해 알림 전송 ex) 현재 시간 20:00인 경우, 알림 시간을 20:00로 설정한 환자에게 알림 전송
     */
    @Scheduled(cron = "0 * * * * *")
    public void FCMSchedule() throws FirebaseMessagingException {
        //현재 요일
        String day = LocalDate.now().getDayOfWeek().name();
        AlarmDayOfWeek dayOfWeek = (AlarmDayOfWeek) Enum.valueOf(AlarmDayOfWeek.class, day);

        //현재 시간
        LocalTime now = LocalTime.now();
        List<String> fcmTokens = alarmListRepository.getAlarmsByDays(dayOfWeek, now, now.plusMinutes(INTERVALMINUTE));

        //알림 전송
        if (fcmTokens.size() > 0) {
            fcmUtil.sendReservationRemindMessages(fcmTokens);
        }
    }
}
