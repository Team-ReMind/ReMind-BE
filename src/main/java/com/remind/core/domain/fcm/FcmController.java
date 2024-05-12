package com.remind.core.domain.fcm;

import com.remind.core.domain.common.response.ApiSuccessResponse;
import com.remind.core.domain.fcm.dto.MessageDto;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {
    private final FcmService fcmService;

//    @SecurityRequirements(value = {})
//    @PostMapping("/fcm/send")
//    public void fcmSendTest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        Long memberId = userDetails.getMemberId();
//        MessageDto message = MessageDto.builder()
//                .memberId(memberId)
//                .title("fcm title in Controller")
//                .content("fcm content in Controller")
//                .build();
//
//
//        fcmService.sendByFcmToken(message);
//    }

    @SecurityRequirements(value = {})
    @PostMapping("/1")
    public ResponseEntity<ApiSuccessResponse<String>> sendMessage1(@RequestBody MessageDto messageDto) throws IOException {
        log.info("msg fcm token1 : "+messageDto.fcmToken());
        log.info("msg title1 : "+messageDto.title());
        log.info("msg body1 : "+messageDto.body());

        fcmService.sendMessage1(
                messageDto);
        return ResponseEntity.ok(new ApiSuccessResponse<>("메세지 발송 성공1"));
//        return ResponseEntity.ok().build();
    }

    @SecurityRequirements(value = {})
    @PostMapping("/2")
    public ResponseEntity<ApiSuccessResponse<String>> sendMessage2(@RequestBody MessageDto messageDto) throws IOException {
        log.info("msg fcm token2 : "+messageDto.fcmToken());
        log.info("msg title2 : "+messageDto.title());
        log.info("msg body2 : "+messageDto.body());

        fcmService.sendMessage2(
                messageDto);
        return ResponseEntity.ok(new ApiSuccessResponse<>("메세지 발송 성공2"));
//        return ResponseEntity.ok().build();
    }
}
