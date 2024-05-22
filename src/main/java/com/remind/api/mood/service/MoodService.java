package com.remind.api.mood.service;

import static com.remind.core.domain.common.enums.ActivityErrorCode.*;
import static com.remind.core.domain.common.enums.MemberErrorCode.*;
import static com.remind.core.domain.common.enums.MoodErrorCode.MOOD_ALREADY_EXIST;
import static com.remind.core.domain.common.enums.MoodErrorCode.MOOD_NOT_FOUND;

import com.remind.api.mood.dto.request.MoodSaveRequestDto;
import com.remind.api.mood.dto.response.ModelActivityResponseDto;
import com.remind.api.mood.dto.response.MoodResponseDto;
import com.remind.core.domain.mood.repository.DateMoodActivityRepository;
import com.remind.core.domain.common.exception.ActivityException;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.common.exception.MoodException;
import com.remind.core.domain.member.Member;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.mood.Activity;
import com.remind.core.domain.mood.Mood;
import com.remind.core.domain.mood.MoodActivity;
import com.remind.core.domain.mood.repository.ActivityRepository;
import com.remind.core.domain.mood.repository.MoodActivityRepository;
import com.remind.core.domain.mood.repository.MoodRepository;
import com.remind.core.security.dto.UserDetailsImpl;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoodService {

    private final MoodActivityRepository moodActivityRepository;
    private final MoodRepository moodRepository;
    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;
    private final DateMoodActivityRepository dateMoodActivityRepository;

    /**
     * 오늘의 기분 기록
     */
    @Transactional
    public Long create(UserDetailsImpl userDetails, MoodSaveRequestDto dto) {

        Member member = memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        // 동일 날짜에 이미 오늘의 기분이 존재하면 예외 처리
        moodRepository.findMoodByPatientAndMoodDate(member.getId(), dto.localDate())
                .stream().findAny().ifPresent((mood) -> {
                    throw new MoodException(MOOD_ALREADY_EXIST);
                });

        Mood mood = Mood.builder()
                .patient(member)
                .feelingType(dto.feelingType())
                .moodDetail(dto.detail())
                .moodDate(dto.localDate())
                .build();

        moodRepository.save(mood);

        /*
         * 오늘의 활동이 존재하면, 각각의 활동에 대해 MoodActivity를 생성한다.
         */
        if (dto.moodActivities() != null) {
            dto.moodActivities().forEach(model -> {

                Activity activity = activityRepository.findById(model.activityId())
                        .orElseThrow(() -> new ActivityException(ACTIVITY_NOT_FOUND));

                MoodActivity moodActivity = MoodActivity.builder()
                        .mood(mood)
                        .activity(activity)
                        .moodActivityDetails(model.detail())
                        .feelingType(model.feelingType())
                        .build();

                moodActivityRepository.save(moodActivity);
            });
        }

        return mood.getId();
    }

    /**
     * 특정 날짜의 오늘의 기분 정보 조회
     */
    @Transactional(readOnly = true)
    public MoodResponseDto get(UserDetailsImpl userDetails, LocalDate localDate) {

        Mood mood = moodRepository.findMoodByPatientAndMoodDate(userDetails.getMemberId(), localDate)
                .orElseThrow(() -> new MoodException(MOOD_NOT_FOUND));

        List<ModelActivityResponseDto> modelActivities = dateMoodActivityRepository.getModelActivities(mood.getId());

        return new MoodResponseDto(mood.getFeelingType(), mood.getMoodDetail(), modelActivities);
    }
}
