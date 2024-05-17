package com.remind.api.member.controller;

import com.remind.api.member.dto.request.KakaoLoginRequest;
import com.remind.api.member.dto.request.OnboardingRequestDto;
import com.remind.api.member.dto.response.CautionPatientsResponseDto;
import com.remind.api.member.dto.response.KakaoLoginResponse;
import com.remind.api.member.dto.request.RefreshTokenRequestDto;
import com.remind.api.member.dto.response.PatientsResponseDto;
import com.remind.api.member.dto.response.TokenResponseDto;
import com.remind.api.member.service.MemberService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.domain.connection.enums.ConnectionStatus;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "member API", description = "member 관련 API 문서")
public class MemberController {

    private final MemberService memberService;
    @Operation(
            summary = "카카오 로그인 API",
            description = "kakao authorization code를 이용하여 발급받은 kakao accessToken을 사용하여 소셜 로그인 합니다."
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
                            @ExampleObject(value = "{\"code\":201, \"message:\": \"정상 처리되었습니다.\", \"data\": {\"userId\": 1, \"roles_type\": ROLE_PATIENT}}")
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

    @Operation(
            summary = "의사/센터의 관리중인 또는 추가 요청(환자 추가하기)인 환자의 목록을 불러오는 api",
            description = "의사/센터가 관리중인 환자 : status=ACCEPT\n의사/센터에 추가 요청이 들어온 환자 : status=PENDING"
    )
    @GetMapping("/patients")
    public ResponseEntity<ApiSuccessResponse<PatientsResponseDto>> getPatientsList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = true) ConnectionStatus status
    ) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(memberService.getPatientsList(userDetails,status)));
    }

//    @Operation(
//            summary = "센터에서 주의가 필요한 환자를 불러오는 api",
//            description = "센터에서 주의가 필요한 환자를 불러오는 api"
//    )
//    @GetMapping("/patients")
//    public ResponseEntity<ApiSuccessResponse<CautionPatientsResponseDto>> getPatientsList(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestParam Boolean caution
//            ) {
//        return ResponseEntity.ok(new ApiSuccessResponse<>(memberService.getPatientsList(userDetails,caution)));
//    }
}
