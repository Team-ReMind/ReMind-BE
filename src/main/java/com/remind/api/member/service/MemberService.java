package com.remind.api.member.service;

import static com.remind.core.domain.enums.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.remind.core.domain.enums.MemberErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static com.remind.core.domain.enums.MemberErrorCode.REFRESH_TOKEN_NOT_MATCH;

import com.remind.api.member.dto.request.KakaoLoginRequest;
import com.remind.api.member.dto.response.KakaoGetMemberInfoResponse;
import com.remind.api.member.dto.response.KakaoLoginResponse;
import com.remind.api.member.exception.MemberException;
//import com.remind.api.member.kakao.KakaoFeignClient;
import com.remind.api.member.kakao.KakaoFeignClient;
import com.remind.core.domain.common.repository.RedisRepository;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.api.member.dto.response.TokenResponseDto;
import com.remind.core.security.dto.UserDetailsImpl;
import com.remind.core.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final KakaoFeignClient kakaoFeignClient;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public KakaoLoginResponse kakaoLogin(KakaoLoginRequest request) {

        String kakaoAccessToken = request.kakaoAccessToken();

        // 카카오어세스토큰으로 카카오api호출, 카카오아이디 받아오기
        // 그 카카오 아이디는 authId이다.
        KakaoGetMemberInfoResponse kakaoMemberInfo = getKakaoIdByAccessToken("Bearer " + kakaoAccessToken);

        log.info("nickname :: " + kakaoMemberInfo.getKakao_account().getProfile().getNickname());
        log.info("authId :: " + kakaoMemberInfo.getAuthId());

        // authId로 찾았을때 존재하지 않으면 등록해주기
        Member member = memberRepository.findByAuthId(kakaoMemberInfo.getAuthId())
                .orElse(null);

        //authId로 등록된 유저가 아니면 가입 후 멤버 반환해주기
        if (member == null) {
            member = register(kakaoMemberInfo);
        }

        //해당 멤버의 authId로 jwt토큰 발급하기

        //이 코드는 이해가 안돼
        UserDetailsImpl userDetail = UserDetailsImpl.fromMember(member);

        String newAccessToken = jwtProvider.createAccessToken(userDetail);
        String newRefreshToken = jwtProvider.createRefreshToken(userDetail);


        // redis 토큰 정보 저장
//        redisRepository.saveToken(userDetail.getMemberId(), newRefreshToken);

        return KakaoLoginResponse.builder()
                .authId(kakaoMemberInfo.getAuthId())
                .redirectUrl(request.redirectUrl())
                .name(kakaoMemberInfo.getKakao_account().getProfile().getNickname())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        // authId로 멤버를 찾을 때, 유저가 존재하면 jwt토큰 발급해주기
    }

    /**
     * Bearer + kakaoAccessToken으로 카카오api를 호출하여 kakao authId를 받아오는 로직
     * @param kakaoAccessToken
     * @return
     */
    private KakaoGetMemberInfoResponse getKakaoIdByAccessToken(String kakaoAccessToken) {
        return kakaoFeignClient.getKakaoIdByAccessToken(kakaoAccessToken);
    }

    /**
     * 카카오 로그인 authId가 존재하지 않는 경우 가입하는 로직
     * @param kakaoMemberInfo
     * @return
     */
    private Member register(KakaoGetMemberInfoResponse kakaoMemberInfo) {
        Member member = Member.builder()
                .authId(kakaoMemberInfo.getAuthId())
                .name(kakaoMemberInfo.getKakao_account().getProfile().getNickname())
                .build();

        return memberRepository.save(member);


    }

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
