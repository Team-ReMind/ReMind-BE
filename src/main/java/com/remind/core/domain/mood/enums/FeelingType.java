package com.remind.core.domain.mood.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeelingType {
    VERY_GOOD("VERY_GOOD", 100), // 매우 좋음
    GOOD("GOOD", 75), // 좋음
    NORMAL("NORMAL", 50), // 보통
    BAD("BAD", 25), // 나쁨
    TERRIBLE("TERRIBLE", 0); // 끔찍함
    final String feeling;
    final int score;
}
