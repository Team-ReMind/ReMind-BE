package com.remind.core.domain.member;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(value = "D")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends Member{
    private String doctorLicenseNumber;
}
