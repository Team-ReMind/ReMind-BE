package com.remind.infra.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FcmConfig {

    @Value("${google.fcm.service.secret}")
    private String GOOGLE_APPLICATION_CREDENTIALS;
    @Value("${google.fcm.service.projectId}")
    private String GOOGLE_PROJECT_ID;

    /**
     * Firebase ADMIN SDK 초기화
     */
    @PostConstruct
    private void init() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(GOOGLE_APPLICATION_CREDENTIALS);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(GOOGLE_PROJECT_ID)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Bean
    public FirebaseMessaging getInstance() {
        return FirebaseMessaging.getInstance();
    }
}
