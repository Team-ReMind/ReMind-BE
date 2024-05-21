package com.remind.api.member.dto.response;


import com.remind.api.member.dto.CenterDto;
import com.remind.api.member.dto.DoctorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "마이페이지 api 반환 객체")
@Builder
public record MyPageResponseDto(
        @Schema(description = "이름")
        String name,

        @Schema(description = "프로필 사진 url")
        String imageUrl,

        @Schema(description = "만 나이")
        int age,

        @Schema(description = "성별")
        String gender,

        @Schema(description = "연관 센터 정보")
        List<CenterDto> centers,

        @Schema(description = "연관 의사 정보")
        List<DoctorDto> doctors

) {
}
