package com.animesh245.emppay.repositories;

import com.animesh245.emppay.entities.BankAccount;
import com.animesh245.emppay.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction getTransactionByTransactionId(Long transactionId);
    List<Transaction> getTransactionsByReceiverId(BankAccount bankAccount);

    @Modifying
    @Query(value = "SELECT * FROM transactions t WHERE t.sender_acc_id = :senderId, DATE(t.transaction_time) BETWEEN :fromDate AND :toDate",nativeQuery = true)
    List<Transaction> getTransactionsBySenderIdAndDateRange(Integer senderId, Date fromDate, Date toDate);

    @Modifying
    @Query(value = "SELECT * FROM transactions t WHERE t.sender_acc_id = :senderId, DATE(t.transaction_time) = :date",nativeQuery = true)
    List<Transaction> getTransactionsBySenderIdAndDate(Integer senderId, Date date);
    Transaction save(Transaction transaction);

    void deleteTransactionByTransactionId(Long transactionId);
}
