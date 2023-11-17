package com.animesh245.emppay.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String userId;
    private String name;
    private String accountNumber;
    private Integer grade;
    private Long balance;
    private String status = "Initiated";
}
