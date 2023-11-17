package com.animesh245.emppay.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    ADMIN(0), EMPLOYEE(1);

    private final int value;
}