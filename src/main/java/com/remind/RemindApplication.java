package com.remind;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class RemindApplication {

    public static void main(String[] args) {
        initEnv();
        SpringApplication.run(RemindApplication.class, args);
    }

    /**
     * 스프링 부트 실행 전 시스템 property를 설정한다.
     */
    static void initEnv() {
        Dotenv.configure()
                .directory("./")
                .filename(".env")
                .load()
                .entries()
                .forEach(e -> {
                    System.setProperty(e.getKey(), e.getValue());
                });
    }
}
