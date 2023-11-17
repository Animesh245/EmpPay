package com.animesh245.emppay.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

public interface Utils {

    public class IsActive {
        public static final int ACTIVE = 1;
        public static final int INACTIVE = 0;
    }
@NoArgsConstructor
    public class EmployeeGrade {
        public static final int GRADE_ONE = 1;
        public static final int GRADE_TWO = 2;
        public static final int GRADE_THREE = 3;
        public static final int GRADE_FOUR = 4;
        public static final int GRADE_FIVE = 5;
        public static final int GRADE_SIX = 6;
    }

    public class AccountType {
        public static final int SAVINGS = 1;
        public static final int CURRENT = 2;
    }
}
