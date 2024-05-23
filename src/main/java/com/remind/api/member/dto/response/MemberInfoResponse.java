package com.remind.api.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MemberInfoResponse(
        @Schema(description = "이름")
        String name,

        @Schema(description = "프로필 사진 url")
        String imageUrl,

        @Schema(description = "만 나이")
        int age,

        @Schema(description = "성별")
        String gender
) {
}
