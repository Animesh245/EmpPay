package com.animesh245.emppay.controllers;

import com.animesh245.emppay.dtos.JwtRequest;
import com.animesh245.emppay.dtos.JwtResponse;
import com.animesh245.emppay.entities.BankAccount;
import com.animesh245.emppay.entities.User;
import com.animesh245.emppay.repositories.BankRepository;
import com.animesh245.emppay.repositories.UserRepository;
import com.animesh245.emppay.services.AuthService;
import com.animesh245.emppay.services.AuthSignInKeyResolver;
import com.animesh245.emppay.services.UserService;
import com.animesh245.emppay.utils.UserType;
import com.animesh245.emppay.utils.Utils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private final BankRepository bankRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest,  HttpServletResponse response) throws Exception {
        User user = userService.getUserByName("admin");
        if(user == null) {
            addAdmin();
        }
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        }catch (UsernameNotFoundException e)
        {
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        UserDetails userDetails = this.userService.loadUserByUsername(jwtRequest.getUsername());

        String token = this.authService.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void addAdmin() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountName("admin");
        bankAccount.setAccountNumber("230123");
        bankAccount.setBankName("Matir Bank");
        bankAccount.setBranchName("This street");
        bankAccount.setAccountType(Utils.AccountType.CURRENT);
        bankAccount.setCurrentBalance(20000L);
        bankRepository.save(bankAccount);

        User admin = new User();
        admin.setUserId(User.generateUUID());
        admin.setBankAccount(bankAccount);
        admin.setName("admin");
        admin.setEmail("admin@email.com");
        admin.setAddress("Head Office");
        admin.setUserTypeRole(UserType.ADMIN);
        admin.setIsActive(Utils.IsActive.ACTIVE);
        admin.setPassword(passwordEncoder.encode("1234"));
        userRepository.save(admin);
    }

    @GetMapping("/secured")
    public String secured(@CookieValue(name = "jwtToken", required = false) String jwtToken) {
        Boolean isValid = authService.isTokenExpired(jwtToken);
        // Check the JWT token in the cookie
        if (jwtToken != null && isValid) {
            return "Access granted to secured resource!";
        } else {
            return "Access denied. Please log in.";
        }
    }
    @GetMapping(value = "/logout")
    String logout(){
        return "Log out page";
    }

//    @PostMapping("/token")
//    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
//        User user = userService.getUserByName("admin");
//        if(user == null) {
//            BankAccount bankAccount = new BankAccount();
//            bankAccount.setAccountName("admin");
//            bankAccount.setAccountNumber("230123");
//            bankAccount.setBankName("Matir Bank");
//            bankAccount.setBranchName("This street");
//            bankAccount.setAccountType(Utils.AccountType.CURRENT);
//            bankAccount.setCurrentBalance(20000L);
//            User admin = new User();
//            admin.setUserId(User.generateUUID());
//            admin.setBankAccount(bankAccount);
//            admin.setName("admin");
//            admin.setUserTypeRole(UserType.ADMIN);
//            admin.setIsActive(Utils.IsActive.ACTIVE);
//            admin.setPassword(passwordEncoder.encode("1234"));
//            userService.saveOrUpdateUser(admin);
//        }
//        try {
//            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
//        }catch (UsernameNotFoundException e)
//        {
//            e.printStackTrace();
//            throw new Exception("Bad Credentials");
//        }
//
//        UserDetails userDetails = this.userService.loadUserByUsername(jwtRequest.getUsername());
//
//        String token = this.authService.generateToken(userDetails);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }
}
