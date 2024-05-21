package com.remind.api.member.dto;

import com.remind.core.domain.member.Doctor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "센터 정보를 반환하는 dto")
@Builder
public record DoctorDto(
        @Schema(description = "의사 위치와 이름")
        String hospitalName,

        @Schema(description = "의사 이름")
        String doctorName
) {
        public static DoctorDto of(Doctor doctor, String name) {
                return DoctorDto.builder()
                        .hospitalName("병원이름")
                        .doctorName(name)
                        .build();
        }
}