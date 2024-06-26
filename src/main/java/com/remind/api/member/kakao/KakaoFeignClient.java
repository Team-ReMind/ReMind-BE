package com.remind.api.member.kakao;

import com.remind.api.member.dto.response.KakaoGetMemberInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-feign", url = "https://kapi.kakao.com")
public interface KakaoFeignClient {

    @GetMapping("/v2/user/me")
    public KakaoGetMemberInfoResponse getKakaoIdByAccessToken(@RequestHeader("Authorization") String kakaoAccessToken);

}
