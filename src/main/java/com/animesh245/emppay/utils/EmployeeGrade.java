package com.animesh245.emppay.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmployeeGrade {
    GRADE_ONE(1),
    GRADE_TWO(2),
    GRADE_THREE(3),
    GRADE_FOUR(4),
    GRADE_FIVE(5),
    GRADE_SIX(6);

    private final int value;
}
