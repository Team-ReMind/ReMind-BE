package com.remind.api.mood.service;

import static com.remind.core.domain.enums.MoodErrorCode.*;

import com.remind.api.mood.dto.MoodChartDto;
import com.remind.api.mood.dto.MoodChartDto.MoodChartResponseDto;
import com.remind.api.mood.dto.response.MoodChartPagingResponseDto;
import com.remind.api.mood.repository.MoodChartPagingRepository;
import com.remind.core.domain.common.exception.MoodException;
import com.remind.core.security.dto.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MoodChartService {

    private final MoodChartPagingRepository moodChartPagingRepository;

    @Transactional(readOnly = true)
    public MoodChartPagingResponseDto getMoodChart(UserDetailsImpl userDetails, Integer year, Integer month) {

        List<MoodChartResponseDto> response = new ArrayList<>();
        List<MoodChartDto> moodChartDtos = moodChartPagingRepository.getMoodChartPaging(userDetails.getMemberId(), year,
                month);

        if (moodChartDtos.isEmpty()) {
            throw new MoodException(MOOD_CHART_NOT_FOUND);
        }

        moodChartDtos.forEach(moodChart -> {
            response.add(moodChart.toResponseDto(moodChart.getFeeling(), moodChart.getLocalDate().getDayOfMonth()));
        });
        return new MoodChartPagingResponseDto(response);
    }
}
