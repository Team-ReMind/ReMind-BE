package com.remind.api.alarm.util.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMUtil {

    private static final String TITLE = "약 리마인드 알림";
    private static final String BODY = "약 드실 시간입니다";
    private final FCMAsyncUtil fcmAsyncUtil;

    /**
     * 약 복용 알림 전송
     */
    public void sendReservationRemindMessages(List<String> tokens) throws FirebaseMessagingException {

        // 최대 500명에게 전송 가능
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(TITLE)
                        .setBody(BODY)
                        .build())
                .addAllTokens(tokens)
                .build();

        fcmAsyncUtil.sendRemindMessage(multicastMessage);
    }
}
