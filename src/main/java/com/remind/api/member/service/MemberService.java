package com.remind.api.member.service;

import static com.remind.core.domain.enums.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.remind.core.domain.enums.MemberErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static com.remind.core.domain.enums.MemberErrorCode.REFRESH_TOKEN_NOT_MATCH;
import com.remind.api.member.exception.MemberException;
import com.remind.core.domain.common.repository.RedisRepository;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.api.member.dto.response.TokenResponseDto;
import com.remind.core.security.dto.UserDetailsImpl;
import com.remind.core.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;
    private final JwtProvider jwtProvider;


    //TODO: NoSql인 redis의 경우, @Transactional을 붙여야하는지 확인
    @Transactional
    public TokenResponseDto refreshToken(String oldRefreshToken, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        // redis 갱신된 refresh token 유효성 검증
        if (!redisRepository.hasKey(member.getId())) {
            throw new MemberException(REFRESH_TOKEN_NOT_FOUND);
        }

        // redis에 저장된 토큰과 비교
        if (!redisRepository.getRefreshToken(member.getId()).get("refreshToken").equals(oldRefreshToken)) {
            throw new MemberException(REFRESH_TOKEN_NOT_MATCH);
        }

        UserDetailsImpl userDetail = UserDetailsImpl.fromMember(member);

        // accessToken, refreshToken 생성
        String newAccessToken = jwtProvider.createAccessToken(userDetail);
        String newRefreshToken = jwtProvider.createRefreshToken(userDetail);

        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        // redis 토큰 정보 저장
        redisRepository.saveToken(userDetail.getMemberId(), newRefreshToken);

        return tokenResponseDto;

    }
}
