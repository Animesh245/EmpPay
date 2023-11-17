package com.animesh245.emppay.services;

import com.animesh245.emppay.entities.BankAccount;
import com.animesh245.emppay.entities.Transaction;
import com.animesh245.emppay.entities.User;
import com.animesh245.emppay.repositories.TransactionRepository;
import com.animesh245.emppay.repositories.UserRepository;
import com.animesh245.emppay.utils.UserType;
import com.animesh245.emppay.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByName(String name) {
        if(StringUtils.isNotBlank(name)) {
            User user = userRepository.getUserByName(name);
            return user;
        }
        return null;
    }

    public User getUserById(String userId) {
        User user = userId != null ? userRepository.getUserByUserId(userId) : null;
        return user;
    }

    public List<User> getAllActiveUsers(int isActive) {
        return userRepository.getAllByIsActive(isActive);
    }

    public String saveOrUpdateUser(User user) {
        String userId = user.getUserId();
        String password = user.getPassword();
        User user2 = getUserById(userId);

        if(user2 == null ) {
            //user.setUserId(User.generateUUID());
            user.setIsActive(Utils.IsActive.ACTIVE);
            user.setUserTypeRole(UserType.EMPLOYEE);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "User data has been saved!";
        } else {
            userRepository.save(user);
            return "User data has been updated!";
        }
    }

    public String deleteUserByUserId(String  userId) {
        User user = userRepository.getUserByUserId(userId);
        if(user != null) {
            if (user.getUserTypeRole() == UserType.ADMIN) {
                userRepository.deleteUserByUserId(userId);
                return user.getName() + " has been deleted.";
            } else {
                userRepository.customUpdateEmployeeStatus(Utils.IsActive.INACTIVE, userId);
                return "successful";
            }
        }
        return null;
    }

    public BankAccount getEmployeeBankAccount(String userId) {
        User user = userRepository.getUserByUserId(userId);
        BankAccount bankAccount = null;

        if(user != null) {
            bankAccount = user.getBankAccount();
        }
        return bankAccount;
    }

    public Long getEmployeeSalary(Long basicGrade,String userId) {
        Long salary = 0L;

        User user = userRepository.getUserByUserId(userId);
        salary = user.getGrade() * 5000L;
        return salary;
    }

    public List<Transaction> getUserTransactions(Integer userId) {
        User user = new User();
        BankAccount bankAccount = user.getBankAccount();
        if(userId != null) {
            return transactionRepository.getTransactionsByReceiverId(bankAccount);
        }
        return null;
    }

    public List<Transaction> getTransactionsBySenderId(Integer senderId, Date fromDate, Date toDate) {
        List<Transaction> transactions = new ArrayList<>();

        if(toDate == null){
            transactions = transactionRepository.getTransactionsBySenderIdAndDate(senderId, fromDate);
        } else {
            transactions = transactionRepository.getTransactionsBySenderIdAndDateRange(senderId, fromDate, toDate);
        }
        return transactions;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User user = userRepository.getUserByEmail(email);

        if(email.equals(user.getEmail()))
        {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
        }else
        {
            throw new UsernameNotFoundException(email);
        }
    }
}
