package com.remind.api.mood.service;

import static com.remind.core.domain.enums.MoodErrorCode.*;

import com.remind.api.mood.dto.MoodChartDto;
import com.remind.api.mood.dto.MoodChartDto.MoodChartResponseDto;
import com.remind.api.mood.dto.response.MoodChartPagingResponseDto;
import com.remind.api.mood.repository.MoodChartPagingRepository;
import com.remind.core.domain.common.exception.MoodException;
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

    @Transactional(readOnly = true)
    public MoodChartPagingResponseDto getMoodChart(UserDetailsImpl userDetails, Integer year, Integer month,
                                                   Integer day, Integer size) {

        List<MoodChartResponseDto> response = new ArrayList<>();
        boolean hasNext = false;
        // 요청한 정보에 대해 커서 기반 pagination
        Slice<MoodChartDto> moodChartDtos = moodChartPagingRepository.getMoodChartPaging2(userDetails.getMemberId(),
                LocalDate.of(year, month, day), Pageable.ofSize(size));

        if (!moodChartDtos.hasContent()) {
            throw new MoodException(MOOD_CHART_NOT_FOUND);
        }
        // 다음 페이지 존재 여부
        if (moodChartDtos.hasNext()) {
            hasNext = true;
        }
        moodChartDtos.getContent().forEach(moodChart -> {
            response.add(moodChart.toResponseDto(moodChart.getFeeling(), moodChart.getLocalDate()));
        });
        return new MoodChartPagingResponseDto(response, hasNext);
    }
}
