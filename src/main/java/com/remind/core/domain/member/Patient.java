package com.remind.core.domain.member;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue(value = "P")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends Member{
    private String protectorPhoneNumber;
}
