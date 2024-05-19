package com.remind.core.domain.member;

import static com.remind.core.domain.common.enums.FixActivityErrorCode.*;
import static com.remind.core.domain.common.enums.MemberErrorCode.*;


import com.remind.core.domain.common.exception.FixActivityException;
import com.remind.core.domain.common.exception.MemberException;
import com.remind.core.domain.member.repository.MemberRepository;
import com.remind.core.domain.mood.Activity;
import com.remind.core.domain.mood.FixActivity;
import com.remind.core.domain.mood.repository.ActivityRepository;
import com.remind.core.domain.mood.repository.FixActivityRepository;
import jakarta.persistence.PostPersist;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class PatientRegisterListener {

    private final FixActivityRepository fixActivityRepository;
    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private static final Long FIX_SIZE = 10L;

    public PatientRegisterListener(@Lazy ActivityRepository activityRepository,
                                   @Lazy FixActivityRepository fixActivityRepository,
                                   @Lazy MemberRepository memberRepository) {
        this.activityRepository = activityRepository;
        this.fixActivityRepository = fixActivityRepository;
        this.memberRepository = memberRepository;

    }

    //TODO: fixActivityRepository.findAll(); 을 사용하면 에러가 발생하는데 해당 에러를 해결하고 리팩토링 할 것
    @PostPersist
    public void persist(Patient patient) {
        Member member = memberRepository.findById(patient.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        List<FixActivity> fixActivities = new ArrayList<>();
        for (long i = 1; i <= FIX_SIZE; i++) {
            fixActivities.add(fixActivityRepository.findById(i)
                    .orElseThrow(() -> new FixActivityException(FIX_ACTIVITY_NOT_FOUND)));
        }
        fixActivities.forEach(activity -> {
            activityRepository.save(
                    Activity.builder()
                            .member(member)
                            .activityName(activity.getActivityName())
                            .activityIcon(activity.getActivityIcon())
                            .build()
            );
        });
    }

}
