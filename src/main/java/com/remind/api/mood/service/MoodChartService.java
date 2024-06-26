package com.remind.api.mood.service;

import com.remind.api.mood.dto.MoodChartDto;
import com.remind.api.mood.dto.MoodChartDto.MoodChartResponseDto;
import com.remind.api.mood.dto.response.ActivityPercentResponseDto;
import com.remind.api.mood.dto.response.MoodChartPagingResponseDto;
import com.remind.api.mood.dto.response.MoodPercentResponseDto;
import com.remind.core.domain.mood.repository.MoodChartPagingRepository;
import com.remind.core.domain.mood.repository.MoodConsecutiveRepository;
import com.remind.core.domain.mood.enums.FeelingType;
import com.remind.core.security.dto.UserDetailsImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MoodChartService {

    private final MoodChartPagingRepository moodChartPagingRepository;
    private final MoodChartCacheService moodChartCacheService;
    private final MoodConsecutiveRepository moodConsecutiveRepository;

    @Transactional(readOnly = true)
    public MoodChartPagingResponseDto getMoodChart(Long memberId, Integer year, Integer month,
                                                   Integer day, Integer size) {

        List<MoodChartResponseDto> response = new ArrayList<>();
        boolean hasNext = false;
        // 요청한 정보에 대해 커서 기반 pagination ( 월의 1일부터 조회하기 위해서는 day = 0 값을 받아야 한다. )
        Slice<MoodChartDto> moodChartDtos = moodChartPagingRepository.getMoodChartPaging2(memberId,
                LocalDate.of(year, month, day + 1), Pageable.ofSize(size));

        // 다음 페이지 존재 여부
        if (moodChartDtos.hasNext()) {
            hasNext = true;
        }
        moodChartDtos.getContent().forEach(moodChart -> {
            response.add(moodChart.toResponseDto(moodChart.getFeeling(), moodChart.getLocalDate()));
        });
        return new MoodChartPagingResponseDto(response, hasNext);
    }

    @Transactional(readOnly = true)
    public List<MoodPercentResponseDto> getActivityChart(Long memberId) {
        return moodChartCacheService.getActivityFeelingTypePercent(memberId);
    }

    @Transactional(readOnly = true)
    public List<ActivityPercentResponseDto> getActivityPercentChart(Long memberId,
                                                                    FeelingType feelingType) {
        return moodChartCacheService.getActivityPercentChart(memberId, feelingType);

    }

    @Transactional(readOnly = true)
    public Long getCurrentSeries(UserDetailsImpl userDetails) {
        return moodConsecutiveRepository.getCurrentSeries(userDetails.getMemberId()).orElse(0L);
    }
}
