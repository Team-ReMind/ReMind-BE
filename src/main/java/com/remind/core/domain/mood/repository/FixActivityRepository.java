package com.remind.core.domain.mood.repository;

import com.remind.core.domain.mood.FixActivity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FixActivityRepository extends JpaRepository<FixActivity, Long> {

    @Query("select fixActivity from FixActivity fixActivity")
    List<FixActivity> findAllCustom();
}
