package com.remind.core.security.config;


import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecuirityConfig {



//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        httpSecuirtySetting(http);
//
//        http
//            .securityMatchers(matcher -> matcher
//                .requestMatchers()
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
            List.of("http://localhost:8080")
        );

        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "PATCH", "DELETE")
        );

        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }

    private void httpSecuirtySetting(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable) //jwt를 사용함으로 csrf 비활성
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // cors 정책 반영
            .sessionManagement(
                session -> session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS)) // jwt를 사용함으로 세션 비활성
            .formLogin(AbstractHttpConfigurer::disable) // form-login 비활성
            .httpBasic(AbstractHttpConfigurer::disable) // basic Authentication 비활성
            .anonymous(AbstractHttpConfigurer::disable); // 익명 사용자 접근 비활성, 모든 요청 인증 필요
    }

}
