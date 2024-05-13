package com.remind.core.domain.fcm;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.enums.MemberErrorCode;
import com.remind.core.domain.fcm.dto.MessageDto;
import com.remind.core.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final MemberRepository memberRepository;

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" +
            "remind-b60cb/messages:send";
    private final ObjectMapper objectMapper;

    // 방식1
    public void sendMessage1(MessageDto messageDto) throws IOException {
        String message = makeMessage(messageDto);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    private String makeMessage(MessageDto messageDto) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(messageDto.fcmToken())
                        .notification(FcmMessage.Notification.builder()
                                .title(messageDto.title())
                                .body(messageDto.body())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "remind-fcm-key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    ///////////////////////////////////////////////////////
    // 방식1
    public void sendMessage2(MessageDto messageDto){
        //추후 userId를통해 가져오는 것으로
        String fcmToken = messageDto.fcmToken();

        //알림 메시지에 들어 갈 noti
        Notification notification = Notification.builder()
                .setTitle(messageDto.title())
                .setBody(messageDto.body())
                .build();

        //메세지
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .putData("data1 지누", "이거뭔데지누1")
                .putData("data2 지누", "이거뭔데지누2")
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            log.info("FCM exception - "+ e.getMessage());
        }
    }


}