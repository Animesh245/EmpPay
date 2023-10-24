package com.animesh245.emppay.controllers;

import com.animesh245.emppay.dtos.JwtRequest;
import com.animesh245.emppay.dtos.JwtResponse;
import com.animesh245.emppay.entities.BankAccount;
import com.animesh245.emppay.entities.User;
import com.animesh245.emppay.services.AuthService;
import com.animesh245.emppay.services.UserService;
import com.animesh245.emppay.utils.AccountType;
import com.animesh245.emppay.utils.IsActive;
import com.animesh245.emppay.utils.UserType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @GetMapping(value = "/login")
    String login() {
        User user = userService.getUserByName("admin");
        if(user == null) {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setAccountName("admin");
            bankAccount.setAccountNumber("230123");
            bankAccount.setBankName("Matir Bank");
            bankAccount.setBranchName("This street");
            bankAccount.setAccountType(AccountType.CURRENT);
            bankAccount.setCurrentBalance(20000L);
            User admin = new User();
            admin.setUserId(User.generateUUID());
            admin.setBankAccount(bankAccount);
            admin.setName("admin");
            admin.setUserTypeRole(UserType.ADMIN);
            admin.setIsActive(IsActive.ACTIVE);
            admin.setPassword(passwordEncoder.encode("1234"));
            userService.saveOrUpdateUser(admin);
        }
        return "Login page";
    }

    @GetMapping(value = "/logout")
    String logout(){
        return "Log out page";
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        jwtRequest.getUsername();
        jwtRequest.getPassword();
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        }catch (UsernameNotFoundException e)
        {
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        UserDetails userDetails = this.userService.loadUserByUsername(jwtRequest.getUsername());

        String token = this.authService.generateToken(userDetails);
        System.out.println("JWT" + token);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
