package com.animesh245.emppay.controllers;

import com.animesh245.emppay.dtos.PaymentRequest;
import com.animesh245.emppay.dtos.PaymentResponse;
import com.animesh245.emppay.entities.BankAccount;
import com.animesh245.emppay.entities.User;
import com.animesh245.emppay.repositories.BankRepository;
import com.animesh245.emppay.repositories.UserRepository;
import com.animesh245.emppay.services.PaymentService;
import com.animesh245.emppay.services.UserService;
import com.animesh245.emppay.utils.UserType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "payment")
public class PaymentController {
    private final UserService userService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    @GetMapping("/initiate")
    ResponseEntity<PaymentResponse> initiatePayment(@RequestParam String userId) {
        User user = userService.getUserById(userId);
        User admin = userRepository.getUserByUserTypeRole(UserType.ADMIN);
        BankAccount bankAccount = admin.getBankAccount();
        PaymentResponse response = new PaymentResponse();
        if (user != null) {
            response.setUserId(user.getUserId());
            response.setName(user.getName());
            response.setGrade(user.getGrade());
            if ( admin != null){
                response.setBalance(bankAccount.getCurrentBalance());
            }
            response.setAccountNumber(user.getBankAccount().getAccountNumber());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    ResponseEntity<String> processPayment(@RequestBody PaymentRequest request) {
        String msg = "";
        if ( request != null) {
            msg = paymentService.processPayment(request.getUserId(), request.getAmount());
        }
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/add-money")
    ResponseEntity<String> addMoney(@RequestBody PaymentRequest request) {
        String msg = "";
        User admin = userRepository.getUserByUserTypeRole(UserType.ADMIN);
        msg = paymentService.addMoney(admin.getUserId(), request.getAmount());

        return ResponseEntity.ok(msg);
    }
}
