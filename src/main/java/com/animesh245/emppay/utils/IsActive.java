package com.animesh245.emppay.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IsActive {
    ACTIVE(0), INACTIVE(1);

    private final int value;
}
