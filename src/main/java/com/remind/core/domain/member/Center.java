package com.remind.core.domain.member;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(value = "C")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Center extends Member{
    private String city;

    private String district;

    private String centerName;
}
