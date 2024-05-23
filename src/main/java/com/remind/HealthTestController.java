package com.remind;

import com.remind.core.domain.common.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//@Tag(name = "Health Check API", description = "Health Check API Controller입니다.")
@RestController
@RequestMapping("api/health")
@SecurityRequirements(value = {})

public class HealthTestController {
    @Operation(summary = "Health Check API", description = "Health Check Api입니다. 빈 리스트 반환!")
    @GetMapping("")
    public ResponseEntity<ApiSuccessResponse<List<String>>> apiHealthTest() {
        return ResponseEntity.ok(new ApiSuccessResponse<>(new ArrayList<>()));
    }

}