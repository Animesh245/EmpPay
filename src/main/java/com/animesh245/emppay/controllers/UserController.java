package com.animesh245.emppay.controllers;

import com.animesh245.emppay.dtos.EmployeeRequestBody;
import com.animesh245.emppay.dtos.EmployeeResponse;
import com.animesh245.emppay.entities.BankAccount;
import com.animesh245.emppay.entities.User;
import com.animesh245.emppay.repositories.BankRepository;
import com.animesh245.emppay.services.UserService;
import com.animesh245.emppay.utils.UserType;
import com.animesh245.emppay.utils.Utils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "user")
public class UserController {
    private final UserService userService;
    private final BankRepository bankRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/save")
    ResponseEntity<String> saveOrUpdateEmployee(@RequestBody EmployeeRequestBody requestBody) {
        String userId = requestBody.getUserId();
        if(userId != null) {
            User user = userService.getUserById(userId);
            BankAccount empBankAccount = userService.getEmployeeBankAccount(userId);
            saveOrUpdateUserAndBankAccountDetails(requestBody, user, empBankAccount);
        }

        User user = new User();
        user.setUserId(User.generateUUID());
        BankAccount empBankAccount = new BankAccount();
        saveOrUpdateUserAndBankAccountDetails(requestBody, user, empBankAccount);
        return ResponseEntity.ok("Data saved");
    }

    private void saveOrUpdateUserAndBankAccountDetails(EmployeeRequestBody requestBody, User user, BankAccount empBankAccount) {
        user.setName(requestBody.getName());
        user.setEmail(requestBody.getEmail());
        String password = requestBody.getPassword();
        if(StringUtils.isEmpty(password)){
            password = "1234";
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setUserTypeRole(UserType.EMPLOYEE);
        user.setAddress(requestBody.getAddress());
        user.setIsActive(Utils.IsActive.ACTIVE);
        user.setGrade(requestBody.getGrade());
        user.setTotalSalary(0L);
        user.setHouseRent(0L);
        user.setMedicalAllowance(0L);
        user.setMobileNumber(requestBody.getMobileNumber());

        empBankAccount.setAccountType(requestBody.getAccountType());
        empBankAccount.setBankName(requestBody.getBankName());
        empBankAccount.setBranchName(requestBody.getBranchName());
        empBankAccount.setAccountName(requestBody.getAccountName());
        empBankAccount.setAccountNumber(requestBody.getAccountNumber());
        empBankAccount.setCurrentBalance(3000L);
        empBankAccount.setUser(user);
        user.setBankAccount(empBankAccount);

        userService.saveOrUpdateUser(user);
        bankRepository.save(empBankAccount);
    }

    @GetMapping("/active")
    ResponseEntity<List<EmployeeResponse>> getAllEmployees(){
        List<User> users = userService.getAllActiveUsers(1);
        List<EmployeeResponse> responseList = new ArrayList<>();
        for(User user: users) {
            EmployeeResponse response = getEmployeeResponse(user, bankRepository);
            responseList.add(response);
        }


        return ResponseEntity.ok(responseList);
    }

    @GetMapping("")
    ResponseEntity<EmployeeResponse> getEmployee(@RequestParam String userId){
        User user = userService.getUserById(userId);
        EmployeeResponse response = getEmployeeResponse(user, bankRepository);

        return ResponseEntity.ok(response);
    }

    private static EmployeeResponse getEmployeeResponse(User user, BankRepository bankRepository) {
        BankAccount empBankAccount = bankRepository.getBankAccountByAccountId(user.getBankAccount().getAccountId());
        EmployeeResponse response = new EmployeeResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setAddress(user.getAddress());
        response.setGrade(user.getGrade());
        response.setName(user.getName());
        response.setTotalSalary(user.getTotalSalary());
        response.setHouseRent(user.getHouseRent());
        response.setAccountNumber(empBankAccount.getAccountNumber());
        response.setMedicalAllowance(user.getMedicalAllowance());
        response.setMobileNumber(user.getMobileNumber());
        response.setUserType(user.getUserTypeRole().name());
        return response;
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteEmployee(@RequestParam String userId) {
        if (userId != null){
            User user = userService.getUserById(userId);
            BankAccount bankAccount = user.getBankAccount();

            userService.deleteUserByUserId(userId);
            bankRepository.deleteBankAccountByAccountId(bankAccount.getAccountId());
            return ResponseEntity.ok("Data deleted");
        }
        return null;
    }
}
