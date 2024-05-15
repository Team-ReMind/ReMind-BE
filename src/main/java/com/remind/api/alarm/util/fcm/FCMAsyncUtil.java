package com.remind.api.alarm.util.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMAsyncUtil {

    private final FirebaseMessaging firebaseMessaging;

    /**
     * 약 복용 알림 전송 (비동기 처리)
     */
    @Async("FCMAsyncBean")
    public void sendRemindMessage(MulticastMessage messages) throws FirebaseMessagingException {
        firebaseMessaging.sendEachForMulticast(messages);
    }
}
