package com.remind.api.member.dto;

import com.remind.core.domain.member.Center;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "센터 정보를 반환하는 dto")
@Builder
public record CenterDto(

        @Schema(description = "센터 위치와 이름")
        String centerName,

        @Schema(description = "센터 담당자")
        String centerManagerName
) {
    public static CenterDto of(Center center, String name) {
        return CenterDto.builder()
                .centerName(center.getCenterName())
                .centerManagerName(name)
                .build();
    }
}