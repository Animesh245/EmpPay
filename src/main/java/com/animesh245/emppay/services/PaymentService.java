package com.animesh245.emppay.services;

import com.animesh245.emppay.entities.BankAccount;
import com.animesh245.emppay.entities.User;
import com.animesh245.emppay.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {
    private final UserRepository userRepository;

     String processPayment(String userId, Long lowestGradeSalary, String adminId) {
        User user = userRepository.getUserByUserId(userId);
        BankAccount empBankAccount = user.getBankAccount();

        User admin = userRepository.getUserByUserId(adminId);
        BankAccount adminBankAccount = admin.getBankAccount();

        Long salary = 0L;
        if(lowestGradeSalary != null) {
            salary = lowestGradeSalary +  (5000 * user.getGrade().getValue());
        }
        if(salary == 0) return null;

        if(salary >= adminBankAccount.getCurrentBalance()) {
            Long adminCurrentBalance = adminBankAccount.getCurrentBalance();
            adminCurrentBalance -= salary;
            adminBankAccount.setCurrentBalance(adminCurrentBalance);

            Long currentBalance = empBankAccount.getCurrentBalance();
            currentBalance += salary;
            user.setTotalSalary(salary);
            empBankAccount.setCurrentBalance(currentBalance);
            return "Payment succuessful.";
        } else {
            Long amount = salary - adminBankAccount.getCurrentBalance();
            return "account out of money! Please add amount = " + amount;
        }
    }

    String addMoney(String adminId, Long amount) {
        User admin = userRepository.getUserByUserId(adminId);
        BankAccount adminBankAccount = admin.getBankAccount();
        Long currentBalance = adminBankAccount.getCurrentBalance();
        currentBalance += amount;

        adminBankAccount.setCurrentBalance(currentBalance);
        return "Amount added to the account";
    }
}
