package com.animesh245.emppay.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountType {
    SAVINGS(1),
    CURRENT(2);

    private final int value;
}
