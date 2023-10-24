package com.animesh245.emppay.entities;

import com.animesh245.emppay.utils.EmployeeGrade;
import com.animesh245.emppay.utils.IsActive;
import com.animesh245.emppay.utils.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements Serializable, UserDetails {
    @Id
    @Column(name = "id", nullable = false)
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "grade")
    private EmployeeGrade grade;

    @Column(name = "address")
    private String address;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private BankAccount bankAccount;

//    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "account_id", referencedColumnName = "id")
//    private BankAccount accountId;
//@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
//@JoinColumn(name = "current_balance", referencedColumnName = "current_balance")
//private BankAccount currentBalance;

    @Column(name = "total_salary")
    private Long totalSalary = 0L;

    @Column(name = "house_rent")
    private Long houseRent;

    @Column(name = "medical_allowance")
    private Long medicalAllowance;

    @Column(name = "is_active")
    private IsActive isActive = IsActive.ACTIVE;

    @Column(name = "user_role")
    private UserType userTypeRole;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        uuidStr = uuidStr.substring(0,4);
        return uuidStr;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> userTypeRole.name());
        return authorities;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
