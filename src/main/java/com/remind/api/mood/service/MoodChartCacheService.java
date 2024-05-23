package com.remind.api.mood.service;

import com.remind.api.mood.dto.ActivityListDto;
import com.remind.api.mood.dto.response.ActivityPercentResponseDto;
import com.remind.api.mood.dto.response.MoodPercentResponseDto;
import com.remind.core.domain.mood.repository.MoodPercentRepository;
import com.remind.core.domain.mood.enums.FeelingType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MoodChartCacheService {

    private final MoodPercentRepository moodPercentRepository;

    /**
     * feelingPercent 이름의 캐시를 사용하고 key는 memberId이다.
     */
    @Cacheable(cacheNames = "feelingPercent", key = "#memberId")
    public List<MoodPercentResponseDto> getActivityFeelingTypePercent(Long memberId) {

        List<MoodPercentResponseDto> response = new ArrayList<>();
        List<FeelingType> feelingResponse = moodPercentRepository.getActivityFeelingTypePercent(memberId);

        // FeelingType 별로 그룹화 이후 각 그룹의 갯수 / 전체 갯수를 계산하고 퍼센트 계산
        feelingResponse.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((feelingType, count) -> {
                    response.add(
                            new MoodPercentResponseDto(feelingType, ((double) count / feelingResponse.size()) * 100));
                });

        return response;
    }

    /**
     * feelingPercent 이름의 캐시를 사용하고 key는 memberId:feelingType.feeling이다.
     */
    @Cacheable(cacheNames = "feelingPercent", key = "#memberId.toString().concat(':').concat(#feelingType.feeling)")
    public List<ActivityPercentResponseDto> getActivityPercentChart(Long memberId, FeelingType feelingType) {

        List<ActivityPercentResponseDto> response = new ArrayList<>();
        List<ActivityListDto> activityResponse = moodPercentRepository.getActivityPercent(feelingType, memberId);

        activityResponse.stream()
                .collect(Collectors.groupingBy(ActivityListDto::name, Collectors.counting()))
                .forEach((name, count) -> {

                    String iconImage = activityResponse.stream()
                            .filter(dto -> dto.name().equals(name))
                            .findAny()
                            .map(ActivityListDto::iconImage)
                            .orElse(null);

                    response.add(new ActivityPercentResponseDto(name, iconImage,
                            ((double) count / activityResponse.size()) * 100));
                });

        return response;
    }
}
