package com.remind.core.security.config;


import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.security.exception.AccessDeniedHandlerImpl;
import com.remind.core.security.filter.JwtAuthenticationFilter;
import com.remind.core.security.filter.JwtAuthenticationHandlerFilter;
import com.remind.core.security.provider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecuirityConfig {

    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * jwt 인증, 인가 적용 o filterChain
     */
    @Bean
    @Order(1) // 우선 순위 1
    public SecurityFilterChain authorizeFilterChain(HttpSecurity http) throws Exception {
        httpSecuirtySetting(http);

        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(authorizeRequestMathcers()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(authorizeRequestMathcers())
                        .hasAnyAuthority(RolesType.ROLE_PATIENT.name(), RolesType.ROLE_DOCTOR.name(),
                                RolesType.ROLE_CENTER.name(), RolesType.ROLE_UNREGISTER.name())
                        .anyRequest().denyAll())
                // jwt 인증/인가 필터 추가
                .addFilterBefore(
                        new JwtAuthenticationFilter(new ProviderManager(jwtAuthenticationProvider)),
                        UsernamePasswordAuthenticationFilter.class)
                // jwt 인증/인가 필터에서 발생한 에러 handle 필터 추가
                .addFilterBefore(new JwtAuthenticationHandlerFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    /**
     * jwt 인증, 인가 적용 x filterChain
     */
    @Bean
    @Order(2) // 우선 순위 2
    public SecurityFilterChain permitAllFilterChain(HttpSecurity http) throws Exception {
        httpSecuirtySetting(http);

        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(permitAllRequestMatchers()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllRequestMatchers()).permitAll()
                        .anyRequest().denyAll()
                );
        return http.build();
    }

    /**
     * security filter 적용 x
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(swaggerRequestMatchers());
    }

    /**
     * authorize endpoint >> 인증/인가 필요한 endpoint
     */
    private RequestMatcher[] authorizeRequestMathcers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(POST, "/member/refresh"),
                antMatcher(POST, "/member/onboarding"),
                antMatcher(GET, "/member/patients"),
                antMatcher(GET, "/member/patients/caution"),
                antMatcher(GET, "/member/myPage"),
                antMatcher(POST, "/activity"),
                antMatcher(GET, "/activity"),
                antMatcher(POST, "/mood"),
                antMatcher(GET, "/mood"),
                antMatcher(GET, "/mood/chart"),
                antMatcher(GET, "/mood/chart/percents"),
                antMatcher(GET, "/mood/chart/percent/activity"),
                antMatcher(POST, "/prescription/relation/request"),
                antMatcher(POST, "/prescription/relation/accept"),
                antMatcher(GET, "/prescription"),
                antMatcher(POST, "/prescription"),
                antMatcher(GET, "/taking-medicine/daily"),
                antMatcher(GET, "/taking-medicine/rate"),
                antMatcher(GET, "/taking-medicine/monthly"),
                antMatcher(POST, "/taking-medicine"),
                antMatcher(POST, "/alarm"),
                antMatcher(GET, "/alarm/{prescriptionId}")

        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * permitAll endpoint >> 인증/인가 필요 x endpoint
     */
    private RequestMatcher[] permitAllRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(POST, "/member/login")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * swagger-ui와 관련된 endpoint >> security filter 적용 x
     */
    private RequestMatcher[] swaggerRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(GET, "/swagger-ui/**"),
                antMatcher(GET, "/v3/api-docs/**")
        );
        return requestMatchers.toArray(RequestMatcher[]::new);
    }


    private void httpSecuirtySetting(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) //jwt를 사용함으로 csrf 비활성
                .sessionManagement(
                        session -> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)) // jwt를 사용함으로 세션 비활성
                .formLogin(AbstractHttpConfigurer::disable) // form-login 비활성
                .httpBasic(AbstractHttpConfigurer::disable) // basic Authentication 비활성
                .anonymous(AbstractHttpConfigurer::disable); // 익명 사용자 접근 비활성, 모든 요청 인증 필요
    }

}
