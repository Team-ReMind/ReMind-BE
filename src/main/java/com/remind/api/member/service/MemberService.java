package com.remind.api.member.service;

import static com.remind.core.domain.enums.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.remind.core.domain.enums.MemberErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static com.remind.core.domain.enums.MemberErrorCode.REFRESH_TOKEN_NOT_MATCH;

import com.remind.api.member.dto.request.KakaoLoginRequest;
import com.remind.api.member.dto.request.OnboardingRequestDto;
import com.remind.api.member.dto.response.KakaoGetMemberInfoResponse;
import com.remind.api.member.dto.response.KakaoLoginResponse;

import com.remind.api.member.dto.response.OnboardingResponseDto;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.api.member.kakao.KakaoFeignClient;
import com.remind.core.domain.common.repository.RedisRepository;
import com.remind.core.domain.enums.MemberErrorCode;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.api.member.dto.response.TokenResponseDto;
import com.remind.core.security.dto.UserDetailsImpl;
import com.remind.core.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

        log.info("name :: " + kakaoMemberInfo.getKakao_account().getName());
        log.info("authId :: " + kakaoMemberInfo.getAuthId());

        // authId로 찾았을때 존재하지 않으면 등록해주기
        Member member = memberRepository.findByAuthId(kakaoMemberInfo.getAuthId())
                .orElse(null);

        //authId로 등록된 유저가 아니면 가입 후 멤버 반환해주기
        if (member == null) {
            member = register(kakaoMemberInfo, request.fcmToken());
            log.info("신규 회원 등록 완료");
        }
        else{
            log.info("기존 회원 로그인 완료");
        }

        //해당 멤버의 authId로 jwt토큰 발급하기

        UserDetailsImpl userDetail = UserDetailsImpl.fromMember(member);

        String newAccessToken = jwtProvider.createAccessToken(userDetail);
        String newRefreshToken = jwtProvider.createRefreshToken(userDetail);

        // redis 토큰 정보 저장
        redisRepository.saveToken(userDetail.getMemberId(), newRefreshToken);

        return KakaoLoginResponse.builder()
                .name(kakaoMemberInfo.getKakao_account().getName())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        // authId로 멤버를 찾을 때, 유저가 존재하면 jwt토큰 발급해주기
    }

    /**
     * Bearer + kakaoAccessToken으로 카카오api를 호출하여 kakao authId를 받아오는 로직
     *
     * @param kakaoAccessToken
     * @return
     */
    private KakaoGetMemberInfoResponse getKakaoIdByAccessToken(String kakaoAccessToken) {
        return kakaoFeignClient.getKakaoIdByAccessToken(kakaoAccessToken);
    }

    /**
     * 카카오 로그인 authId가 존재하지 않는 경우 가입하는 로직
     *
     * @param kakaoMemberInfo
     * @return
     */
    private Member register(KakaoGetMemberInfoResponse kakaoMemberInfo,String fcmToken) {
        System.out.println("year: " + kakaoMemberInfo.getKakao_account().getBirthyear());
        int currentYear = LocalDate.now().getYear();
        int birthYearInt = Integer.parseInt(kakaoMemberInfo.getKakao_account().getBirthyear());
        int age = currentYear - birthYearInt;
        Member member = Member.builder()
                .authId(kakaoMemberInfo.getAuthId())
                .name(kakaoMemberInfo.getKakao_account().getName())
                .age(age)
                .gender(kakaoMemberInfo.getKakao_account().getGender())
                .email(kakaoMemberInfo.getKakao_account().getEmail())
                .phoneNumber(kakaoMemberInfo.getKakao_account().getPhone_number())
                .profileImageUrl(kakaoMemberInfo.getKakao_account().getProfile().getProfile_image_url())
                .isOnboardingFinished(false)
                .fcmToken(fcmToken)
                .build();
        System.out.println("year: " + kakaoMemberInfo.getKakao_account().getBirthyear());
        return memberRepository.save(member);


    }

    /**
     * 로그인 후, 온보딩이 완료되었을 때 엔티티의컬럼을 업데이트 하는 로직
     * @param userDetails
     * @param req
     * @return
     */
    @Transactional
    public OnboardingResponseDto onboarding(UserDetailsImpl userDetails, OnboardingRequestDto req) {
        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        //환자, 센터, 의사인 경우
        if (req.rolesType() == RolesType.ROLE_USER) {
            member.updateRolesTypeForUser(RolesType.ROLE_USER, req.protectorPhoneNumber());
        } else if (req.rolesType() == RolesType.ROLE_CENTER) {
            member.updateRolesTypeForCenter(RolesType.ROLE_CENTER, req.city(), req.district(), req.centerName());
        } else if (req.rolesType() == RolesType.ROLE_DOCTOR) {
            member.updateRolesTypeForDoctor(RolesType.ROLE_DOCTOR);
        }

        return OnboardingResponseDto.builder()
                .userId(member.getId())
                .rolesType(member.getRolesType())
                .build();
    }



    @Transactional
    public TokenResponseDto refreshToken(String oldRefreshToken, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        // redis 갱신된 refresh token 유효성 검증
        if (!redisRepository.hasKey(member.getId())) {
            throw new MemberException(REFRESH_TOKEN_NOT_FOUND);
        }
        // redis에 저장된 토큰과 비교
        if (!redisRepository.getRefreshToken(member.getId()).get(RedisRepository.REFRESH_TOKEN_KEY)
                .equals(oldRefreshToken)) {
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
