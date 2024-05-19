package com.remind.core.domain.statistic;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;






}
