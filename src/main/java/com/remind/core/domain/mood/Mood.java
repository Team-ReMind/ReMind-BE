package com.remind.core.domain.mood;

import com.remind.core.domain.member.Member;
import com.remind.core.domain.mood.enums.FeelingType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Member patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "feeling_type", nullable = false)
    private FeelingType feelingType;

    @Column(name = "mood_detail", nullable = true)
    private String moodDetail;

    @Column(name = "mood_date", nullable = false)
    private LocalDate moodDate;
}
