package com.remind.api.member.kakao;

import com.remind.api.member.dto.response.KakaoGetMemberInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-feign", url = "https://kapi.kakao.com")
public interface KakaoFeignClient {

    @GetMapping("/v2/user/me")
//    @GetMapping("/v1/user/access_token_info")
    public KakaoGetMemberInfoResponse getKakaoIdByAccessToken(@RequestHeader("Authorization") String kakaoAccessToken);

//    @GetMapping("/v2/user/me")
//    public KakaoGetUserInfoResponseDto getKakaoUserEmailByAccessToken(@RequestHeader("Authorization") String accessToken);
}
