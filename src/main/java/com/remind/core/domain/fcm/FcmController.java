package com.remind.core.domain.fcm;

import com.remind.core.domain.fcm.dto.MessageDto;
import com.remind.core.domain.fcm.dto.RequestDto;
import com.remind.core.security.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @SecurityRequirements(value = {})
    @PostMapping("/fcm/send")
    public void fcmSendTest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMemberId();
        MessageDto message = MessageDto.builder()
                .memberId(memberId)
                .title("fcm title in Controller")
                .content("fcm content in Controller")
                .build();


        fcmService.sendByFcmToken(message);
    }

    @SecurityRequirements(value = {})
    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody RequestDto requestDto) throws IOException {
        System.out.println(requestDto.getTargetToken() + " "
                +requestDto.getTitle() + " " + requestDto.getBody());

        fcmService.sendMessageTo(
                requestDto.getTargetToken(),
                requestDto.getTitle(),
                requestDto.getBody());
        return ResponseEntity.ok().build();
    }
}
