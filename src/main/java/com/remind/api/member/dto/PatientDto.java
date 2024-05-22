package com.remind.api.member.dto;

import com.remind.core.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PatientDto(
        @Schema(description = "멤버 아이디")
        Long memberId,

        @Schema(description = "멤버 이름")
        String name,

        @Schema(description = "멤버 성별")
        String gender,

        @Schema(description = "멤버 출생 년도")
        int birthYear

) {
        public static PatientDto of(Member member) {
                return PatientDto.builder()
                        .memberId(member.getId())
                        .name(member.getName())
                        .gender(member.getGender())
                        .birthYear(member.getBirthday().getYear())
                        .build();
        }
}
