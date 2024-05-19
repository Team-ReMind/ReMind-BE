package com.remind.core.domain.member.repository;

import com.remind.core.domain.member.Center;
import com.remind.core.domain.member.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterRepository extends JpaRepository<Center,Long> {

}
