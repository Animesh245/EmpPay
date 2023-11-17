package com.animesh245.emppay.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponse {
    private String userId;
    private String name;
    private String email;
    private int grade;
    private String address;
    private String mobileNumber;
    private String accountNumber;
    private Long totalSalary;
    private Long houseRent;
    private Long medicalAllowance;
    private String userType;
}
