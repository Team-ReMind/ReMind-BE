package com.remind.core.domain.mood.repository;

import com.remind.core.domain.mood.Mood;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoodRepository extends JpaRepository<Mood, Long> {
    @Query("select mood from Mood mood where mood.patient.id = :patientId and mood.moodDate = :moodDate")
    Optional<Mood> findMoodByPatientAndMoodDate(@Param("patientId") Long patientId,
                                                @Param("moodDate") LocalDate moodDate);

}
