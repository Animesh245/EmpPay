package com.animesh245.emppay.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String userId;
    private Long amount;
}
