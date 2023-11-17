package com.animesh245.emppay.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestBody {
    private String userId;
    private String name;
    private String email;
    private String password;
    private int grade;
    private String address;
    private String mobileNumber;
    private int accountType;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String branchName;
}
