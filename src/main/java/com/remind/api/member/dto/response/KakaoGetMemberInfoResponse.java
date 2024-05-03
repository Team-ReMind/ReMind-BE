package com.remind.api.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoGetMemberInfoResponse {
    @JsonProperty("id")
    private Long authId;

    private KakaoAcountDto kakao_account;

    /**
     * kakao api 응답에 들어가는 객체
     */
    @Getter
    public static class KakaoAcountDto {

        private KakaoProfileDto profile;
        private String name;
        private String email;
        private String birthYear;
        private String gender;
        private String phone_number;

        // 추가되는 필드
    }

    @Getter
    public static class KakaoProfileDto {

//        private String nickname;
        private String profile_image_url;

    }


}
