package com.remind.api.member.controller;

import com.remind.api.member.dto.request.KakaoLoginRequest;
import com.remind.api.member.dto.response.KakaoLoginResponse;
import com.remind.api.member.dto.request.RefreshTokenRequestDto;
import com.remind.api.member.dto.response.TokenResponseDto;
import com.remind.api.member.service.MemberService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
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
            description = "kakao access token을 사용하여 소셜 로그인 시도"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiSuccessResponse<KakaoLoginResponse>> kakaoLogin(
            @RequestBody KakaoLoginRequest req
    ) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(memberService.kakaoLogin(req))
        );

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
