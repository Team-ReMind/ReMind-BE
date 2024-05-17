package com.remind.api.connection.controller;

import com.remind.api.connection.dto.reqeust.AcceptConnectionRequestDto;
import com.remind.api.connection.service.ConnectionService;
import com.remind.api.prescription.dto.request.CreatePrescriptionRequestDto;
import com.remind.api.connection.dto.reqeust.RequestConnectionRequestDto;
import com.remind.api.connection.dto.response.AcceptConnectionResponseDto;
import com.remind.api.prescription.dto.response.CreatePrescriptionResponseDto;
import com.remind.api.connection.dto.response.RequestConnectionResponseDto;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prescription")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Connection API", description = "의사/센터 - 환자 커넥션 관련 API")
public class ConnectionController {

    private final ConnectionService connectionService;

    @Operation(
            summary = "환자 -> 의사/센터 관계 요청 API",
            description = "환자가 의사/센터의 멤버코드를 이용하여 관계를 요청하는 api"
    )
    @PostMapping("/relation/request")
    public ResponseEntity<ApiSuccessResponse<RequestConnectionResponseDto>> requestRelataion(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody RequestConnectionRequestDto req) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(connectionService.requestConnection(userDetails, req)));
    }

    @Operation(
            summary = "의사/센터 -> 환자 관계 수락 API",
            description = "의사/센터가 환자의 관계 요청을 수락하는 api"
    )
    @PostMapping("/relation/accept")
    public ResponseEntity<ApiSuccessResponse<AcceptConnectionResponseDto>> AcceptRelataion(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AcceptConnectionRequestDto req) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(connectionService.acceptConnection(userDetails, req)));
    }



}
