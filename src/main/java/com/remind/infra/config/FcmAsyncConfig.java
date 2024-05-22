package com.remind.infra.config;

import java.util.concurrent.Executor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Fcm 비동기 처리 thread pool 설정입니다.
 */
@Configuration
public class FcmAsyncConfig implements AsyncConfigurer {

    /*
     트래픽 파악 후, 유동적으로 변경 필요함
     */
    private final Integer CORE_POOL_SIZE = 10;
    private final Integer QUEUE_CAPACITY = 10;
    private final Integer MAX_POOL_SIZE = 100;
    private final Integer KEEP_ALIVE_TIME = 30;
    private final String THREAD_NAME_PREFIX = "FCM-ASYNC-THREAD";


    @Bean("FCMAsyncBean")
    @Override
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        threadPoolTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        threadPoolTaskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }

    // TODO: 비동기 과정에서 발생한 예외 처리 추가하기
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }

}
