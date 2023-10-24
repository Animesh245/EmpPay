package com.animesh245.emppay.repositories;

import com.animesh245.emppay.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<BankAccount, Long> {
    BankAccount getBankAccountByAccountId(Long accountId);
    BankAccount getBankAccountByAccountNumber(String accountNumber);

//    @Query(value = "SELECT * FROM BankAccount WHERE " ,nativeQuery = true)
//    BankAccount getBankAccountByUserId(Integer userId);
    BankAccount save(BankAccount bankAccount);
    void deleteBankAccountByAccountId(Long accountId);
}
