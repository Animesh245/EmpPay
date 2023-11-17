package com.animesh245.emppay.services;

import com.animesh245.emppay.entities.BankAccount;
import com.animesh245.emppay.entities.Transaction;
import com.animesh245.emppay.entities.User;
import com.animesh245.emppay.repositories.TransactionRepository;
import com.animesh245.emppay.repositories.UserRepository;
import com.animesh245.emppay.utils.UserType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

     public String processPayment(String userId, Long lowestGradeSalary) {
         Transaction transaction = new Transaction();
        User user = userRepository.getUserByUserId(userId);
        BankAccount empBankAccount = user.getBankAccount();
        transaction.setReceiverId(empBankAccount.getAccountId());

        User admin = userRepository.getUserByUserTypeRole(UserType.ADMIN);
        transaction.setSenderId(admin.getUserId());
        BankAccount adminBankAccount = admin.getBankAccount();
        Long adminCurrentBalance = adminBankAccount.getCurrentBalance();
        Long salary = 0L;
        if(lowestGradeSalary != null) {
            int grade = user.getGrade();
            if(grade > 0) {
                salary = lowestGradeSalary + (5000 * grade) ;
            } else {
                salary = lowestGradeSalary;
            }
        }
        if(salary == 0) return null;

        if(salary <= adminCurrentBalance) {
            adminCurrentBalance -= salary;
            adminBankAccount.setCurrentBalance(adminCurrentBalance);

            Long currentBalance = empBankAccount.getCurrentBalance();
            currentBalance += salary;
            user.setTotalSalary(salary);
            Long houseRent = (salary * 20)/100;
            Long medicalAllowance = (salary * 15) / 100;

            empBankAccount.setCurrentBalance(currentBalance);

            transaction.setAmount(salary);
            transaction.setStatus("Successful");
            transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
            transactionRepository.save(transaction);

            user.setHouseRent(houseRent);
            user.setMedicalAllowance(medicalAllowance);
            return "Payment successful.";
        } else {
            Long amount = salary - adminBankAccount.getCurrentBalance();
            transaction.setAmount(0L);
            transaction.setStatus("Failed");
            transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
            transactionRepository.save(transaction);
            return "account out of money! Please add amount = " + amount;
        }
    }

    public String addMoney(String adminId, Long amount) {
        User admin = userRepository.getUserByUserId(adminId);
        if (admin.getUserTypeRole() != UserType.ADMIN) {
            admin = userRepository.getUserByUserTypeRole(UserType.ADMIN);
        }
        BankAccount adminBankAccount = admin.getBankAccount();
        Long currentBalance = adminBankAccount.getCurrentBalance();
        currentBalance += amount;

        adminBankAccount.setCurrentBalance(currentBalance);
        return "Amount added to the account";
    }
}
