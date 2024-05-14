package com.remind.api.prescription.controller;

import com.remind.api.prescription.dto.request.CreatePrescriptionRequestDto;
import com.remind.api.prescription.dto.response.CreatePrescriptionResponseDto;
import com.remind.api.prescription.service.PrescriptionService;
import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prescription")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "의사 - 환자와 관련있는 API", description = "prescription(약 복용 정보, 처방 정보, 의사-환자 관계 관련 API 문서")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

//    @Operation(
//            summary = "환자 -> 의사 관계 요청 API",
//            description = "환자가 의사의 멤버코드를 이용하여 관계를 요청하는 api"
//    )
//    @PostMapping("/relation/request")
//    public ResponseEntity<ApiSuccessResponse<RequestRelationResponseDto>> requestRelataion(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @Valid @RequestBody RequestRelationRequestDto req) {
//        return ResponseEntity.ok(new ApiSuccessResponse<>(prescriptionService.requestRelation(userDetails, req)));
//    }
//
//    @Operation(
//            summary = "의사 -> 환자 관계 수락 API",
//            description = "의사가 환자의 관계 요청을 수락하는 api"
//    )
//    @PostMapping("/relation/accept")
//    public ResponseEntity<ApiSuccessResponse<AcceptRelationResponseDto>> AcceptRelataion(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @Valid @RequestBody AcceptRelationRequestDto req) {
//        return ResponseEntity.ok(new ApiSuccessResponse<>(prescriptionService.acceptRelation(userDetails, req)));
//    }

    @Operation(
            summary = "의사,센터가 약 복용 정보 생성하는 api",
            description = "의사,센터가 약 복용 정보 생성하는 api"
    )
    @PostMapping("")
    public ResponseEntity<ApiSuccessResponse<CreatePrescriptionResponseDto>> createPrescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreatePrescriptionRequestDto req) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(prescriptionService.createPrescription(userDetails, req)));
    }


}
