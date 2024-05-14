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
@Tag(name = "Prescription(약 처방 정보) API", description = "prescription(약 복용 정보, 처방 정보) API 문서")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;


    //밥먹고 수정
    @Operation(
            summary = "의사가 약 복용 정보 생성하는 api",
            description = "의사가 약 복용 정보 생성하는 api"
    )
    @PostMapping("")
    public ResponseEntity<ApiSuccessResponse<CreatePrescriptionResponseDto>> createPrescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreatePrescriptionRequestDto req) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(prescriptionService.createPrescription(userDetails, req)));
    }


}
