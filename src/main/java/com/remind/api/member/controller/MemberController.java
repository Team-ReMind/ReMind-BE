package com.remind.api.member.controller;

import com.remind.api.member.dto.request.RefreshTokenRequestDto;
import com.remind.api.member.service.MemberService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.security.dto.UserDetailsImpl;
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
public class MemberController {

    private final MemberService memberService;

    //>>>>  소셜 로그인 구현 예정
    @PostMapping("/login")
    public void login(){

    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiSuccessResponse<?>> refreshToken(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody RefreshTokenRequestDto dto
    ) {
        return ResponseEntity.ok(
                new ApiSuccessResponse<>(memberService.refreshToken(dto.refreshToken(), userDetails.getMemberId())));
    }
}
