package com.animesh245.emppay.dtos;

import com.animesh245.emppay.utils.EmployeeGrade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestBody {
    private String userId;
    private String name;
    private String password;
    private String grade;
    private String address;
    private String mobileNumber;
    private String accountType;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String branchName;
}
