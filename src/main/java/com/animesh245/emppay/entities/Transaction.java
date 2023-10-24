package com.animesh245.emppay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long transactionId;

    @Column(name = "receiver_acc_id")
    private Long receiverId;

    @Column(name = "sender_acc_id")
    private Long senderId;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "transaction_time")
    private Timestamp transactionTime;
}
