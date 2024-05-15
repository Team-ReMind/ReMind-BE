package com.remind.api.mood.service;

import static com.remind.core.domain.common.enums.MemberErrorCode.*;

import com.remind.api.mood.dto.request.ActivitySaveRequestDto;
import com.remind.api.mood.dto.ActivityListDto;
import com.remind.api.mood.dto.response.ActivityListResponseDto;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.enums.RolesType;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.mood.Activity;
import com.remind.core.domain.mood.repository.ActivityRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;

    /**
     * 활동 추가
     */
    @Transactional
    public Long save(UserDetailsImpl userDetails, ActivitySaveRequestDto dto) {
        // 환자만 접근 가능
        if (!validateUserRole(userDetails)) {
            throw new MemberException(MEMBER_UNAUTHORIZED);
        }

        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Activity activity = Activity.builder()
                .member(member)
                .activityName(dto.name())
                .activityIcon(dto.iconImage())
                .build();

        activityRepository.save(activity);

        return activity.getId();
    }

    /**
     * 추가한 활동 리스트 조회
     */
    @Transactional(readOnly = true)
    public ActivityListResponseDto getActivityList(UserDetailsImpl userDetails) {
        // 환자만 접근 가능
        if (!validateUserRole(userDetails)) {
            throw new MemberException(MEMBER_UNAUTHORIZED);
        }

        List<ActivityListDto> activities = activityRepository.findActivitiesByMemberId(userDetails.getMemberId())
                .stream()
                .map(ActivityListDto::fromEntity)
                .collect(Collectors.toList());

        return new ActivityListResponseDto(activities);
    }

    private Boolean validateUserRole(UserDetailsImpl userDetails) {
        return RolesType.validateUserRole(userDetails.getAuthorities());
    }
}
