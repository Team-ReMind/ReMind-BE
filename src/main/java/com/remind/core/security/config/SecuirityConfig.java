package com.remind.core.security.config;


import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.security.exception.AccessDeniedHandlerImpl;
import com.remind.core.security.exception.AuthenticationEntryPointImpl;
import com.remind.core.security.filter.JwtAuthenticationFilter;
import com.remind.core.security.filter.JwtAuthenticationHandlerFilter;
import com.remind.core.security.jwt.JwtProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecuirityConfig {

    private final JwtProvider jwtProvider;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;


    /**
     * jwt 인증, 인가 적용 x filterChain
     */
    @Bean
    @Order(1)
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
     * jwt 인증, 인가 적용 o filterChain
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authorizeFilterChain(HttpSecurity http) throws Exception {
        httpSecuirtySetting(http);

        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(authorizeRequestMathcers()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(authorizeRequestMathcers())
                        .hasAuthority(RolesType.ROLE_USER.name())
                        .anyRequest().denyAll())
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationHandlerFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling(handler ->
                        handler
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    /**
     * authorize endpoint >> 인증/인가 필요한 endpoint
     */
    private RequestMatcher[] authorizeRequestMathcers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(POST, "/member/refresh")
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
