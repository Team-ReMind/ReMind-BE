package com.remind.core.infra.config;

import com.remind.RemindApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = RemindApplication.class)
@Configuration
public class FeignClientConfig {
}