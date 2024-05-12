package com.remind.api.member.controller;

import com.remind.api.member.dto.request.KakaoLoginRequest;
import com.remind.api.member.dto.request.OnboardingRequestDto;
import com.remind.api.member.dto.response.KakaoLoginResponse;
import com.remind.api.member.dto.request.RefreshTokenRequestDto;
import com.remind.api.member.dto.response.TokenResponseDto;
import com.remind.api.member.service.MemberService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "member API", description = "member 관련 API 문서")
public class MemberController {

    private final MemberService memberService;
    @Operation(
            summary = "카카오 로그인 API",
            description = "kakao authorization code를 이용하여 발급받은 kakao accessToken을 사용하여 소셜 로그인 합니다.\n 기기 인증용 fcm Token도 같이 전송합니다."
    )
    @PostMapping("/login")
    @SecurityRequirements(value = {})
    public ResponseEntity<ApiSuccessResponse<KakaoLoginResponse>> kakaoLogin(
            @RequestBody KakaoLoginRequest req
    ) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(memberService.kakaoLogin(req))
        );

    }

    @Operation(
            summary = "로그인 후 온보딩 완료 API"
    )
    @ApiResponse(
            responseCode = "201", description = "온보딩 정보 추가 성공 응답입니다.",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = "{\"code\":201, \"message:\": \"정상 처리되었습니다.\", \"data\": {\"userId\": 1, \"roles_type\": ROLE_USER}}")
                    }
            )
    )
    @PostMapping("/onboarding")
    public ResponseEntity<ApiSuccessResponse<?>> onboarding(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody OnboardingRequestDto onboardingRequestDto
    ) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(memberService.onboarding(userDetails, onboardingRequestDto)));
    }


    @Operation(
            summary = "토큰 갱신 API",
            description = "refresh token을 사용하여 access token와 refresh token을 갱신한다."
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiSuccessResponse<TokenResponseDto>> refreshToken(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody RefreshTokenRequestDto dto
    ) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(memberService.refreshToken(dto.refreshToken(), userDetails.getMemberId())));
    }
}
