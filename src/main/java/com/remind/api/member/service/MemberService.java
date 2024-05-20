package com.remind.api.member.service;

import com.remind.api.member.dto.CautionPatientDto;
import com.remind.api.member.dto.PatientDto;
import com.remind.api.member.dto.request.KakaoLoginRequest;
import com.remind.api.member.dto.request.OnboardingRequestDto;
import com.remind.api.member.dto.response.*;
import com.remind.api.member.kakao.KakaoFeignClient;
import com.remind.core.domain.common.enums.MemberErrorCode;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.repository.RedisRepository;
import com.remind.core.domain.connection.enums.ConnectionStatus;
import com.remind.core.domain.member.Center;
import com.remind.core.domain.member.Doctor;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.Patient;
import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.domain.member.repository.CenterRepository;
import com.remind.core.domain.member.repository.DoctorRepository;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.member.repository.PatientRepository;
import com.remind.core.domain.mood.Activity;
import com.remind.core.domain.mood.repository.ActivityRepository;
import com.remind.core.domain.mood.repository.FixActivityRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import com.remind.core.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.remind.core.domain.common.enums.MemberErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final KakaoFeignClient kakaoFeignClient;
    private final MemberRepository memberRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final CenterRepository centerRepository;
    private final RedisRepository redisRepository;
    private final JwtProvider jwtProvider;
    private final FixActivityRepository fixActivityRepository;
    private final ActivityRepository activityRepository;

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
            member = register(kakaoMemberInfo);

            log.info("등록된 회원이 아닙니다. 회원가입 진행 중이요");

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
                .rolesType(member.getRolesType())
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
    private Member register(KakaoGetMemberInfoResponse kakaoMemberInfo) {
//
//        String memberCode = createMemberCode();
//        Member member = Member.builder()
//                .authId(kakaoMemberInfo.getAuthId())
//                .name(kakaoMemberInfo.getKakao_account().getName())
//                .age(age)
//                .gender(kakaoMemberInfo.getKakao_account().getGender())
//                .email(kakaoMemberInfo.getKakao_account().getEmail())
//                .phoneNumber(kakaoMemberInfo.getKakao_account().getPhone_number())
//                .profileImageUrl(kakaoMemberInfo.getKakao_account().getProfile().getProfile_image_url())
//                .memberCode(memberCode)
//                .rolesType(RolesType.ROLE_UNREGISTER)
//                .build();
//        return memberRepository.save(member);


        String memberCode = createMemberCode();
        Member member = Member.builder()
                .authId(kakaoMemberInfo.getAuthId())
                .profileImageUrl(kakaoMemberInfo.getKakao_account().getProfile().getProfile_image_url())
                .memberCode(memberCode)
                .rolesType(RolesType.ROLE_UNREGISTER)
                .build();
        return memberRepository.save(member);

    }

    /**
     * 각 멤버마다 6자리의 독립된 코드 번호를 생성하는 로직
     * @return
     */
    private String createMemberCode() {
        String memberCode = "";

        do {
            memberCode = "";
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                int randomNumber = random.nextInt(36); // 0~9, A~Z
                if (randomNumber < 10) {
                    memberCode += Integer.toString(randomNumber); //0~9
                } else {
                    memberCode += String.valueOf((char) (randomNumber - 10 + 'A')); //A~Z
                }
            }
            log.info("memberCode : " + memberCode);
        }
        while (memberRepository.findByMemberCode(memberCode).isPresent());

        return memberCode;
    }

    /**
     * 로그인 후, 온보딩이 완료되었을 때 엔티티의 컬럼을 업데이트 하는 로직
     * @param userDetails
     * @param req
     * @return
     */
    @Transactional
    public OnboardingResponseDto onboarding(UserDetailsImpl userDetails, OnboardingRequestDto req) {
        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        //온보딩 추가 정보 등록
        LocalDate birthday = Member.birthConverter(req.birthday());
        member.updateInfo(req.name(),req.gender(),req.phoneNumber(),birthday);
        //이미 온보딩 된환자 예외처리 로직 추가

        //환자, 센터, 의사인 경우
        if (req.rolesType() == RolesType.ROLE_PATIENT) {
            member.updateRolesTypeAndFcmToken(RolesType.ROLE_PATIENT, req.fcmToken());
            Patient patient = Patient.builder()
                    .protectorPhoneNumber(req.protectorPhoneNumber())
                    .member(member)
                    .build();
            patientRepository.save(patient);
            fixActivityRepository.findAll().forEach(activity -> {
                activityRepository.save(
                        Activity.builder()
                                .activityName(activity.getActivityName())
                                .activityIcon(activity.getActivityIcon())
                                .member(member)
                                .build()
                );
            });

        } else if (req.rolesType() == RolesType.ROLE_CENTER) {
            member.updateRolesTypeAndFcmToken(RolesType.ROLE_CENTER, req.fcmToken());
            Center center = Center.builder()
                    .centerName(req.centerName())
                    .city(req.city())
                    .district(req.district())
                    .member(member)
                    .build();
            centerRepository.save(center);

        } else if (req.rolesType() == RolesType.ROLE_DOCTOR) {
            member.updateRolesTypeAndFcmToken(RolesType.ROLE_DOCTOR, req.fcmToken());
            Doctor doctor = Doctor.builder()
                    .doctorLicenseNumber(req.doctorLicenseNumber())
                    .member(member)
                    .build();
            doctorRepository.save(doctor);
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

    /**
     * 의사, 센터가 관리중인 환자의 리스트를 불러오는 로직
     * @param userDetails
     * @param status
     * @return
     */
    @Transactional(readOnly = true)
    public PatientsResponseDto getPatientsList(UserDetailsImpl userDetails, ConnectionStatus status) {
        //조회하는 사람 정보 조회
        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        //의사 또는 센터가 아니면 조회 불가
        if (!member.getRolesType().equals(RolesType.ROLE_DOCTOR) &&
                !member.getRolesType().equals(RolesType.ROLE_CENTER)) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_DOCTOR_OR_CENTER);
        }

        //dto 리스트
        List<Member> memberList = memberRepository.findPatientInfoByTargetMemberIdAndStatus(member.getId(),
                status);

        List<PatientDto> patientDtos = memberList.stream().map(PatientDto::of).toList();

        return PatientsResponseDto.builder()
                .patientDtos(patientDtos)
                .patientNumber(patientDtos.size())
                .targetMemberCode(member.getMemberCode())
                .build();

    }


    /**
     * 센터가 관리중인 위험도가 높은 환자의 리스트를 불러오는 로직
     * @param userDetails
     * @return
     */
    @Transactional(readOnly = true)
    public CautionPatientsResponseDto getCautionPatientsList(UserDetailsImpl userDetails) {
        //조회하는 사람 정보 조회
        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        //의사 또는 센터가 아니면 조회 불가
        if (!member.getRolesType().equals(RolesType.ROLE_DOCTOR) &&
                !member.getRolesType().equals(RolesType.ROLE_CENTER)) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_DOCTOR_OR_CENTER);
        }

        //dto 리스트. 통계 테이블 추가 후 수정
//        List<CautionPatientDto> cautionPatientDtos = memberRepository.findCautionPatients(member.getId());
        List<CautionPatientDto> cautionPatientDtos = new ArrayList<>();
        return CautionPatientsResponseDto.builder()
                .cautionPatientDtos(cautionPatientDtos)
                .patientNumber(cautionPatientDtos.size())
                .build();

    }



}
