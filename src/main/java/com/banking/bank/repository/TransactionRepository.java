package com.banking.bank.repository;

import com.banking.bank.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);

    @Transactional
    void deleteByFromAccountOrToAccount(String fromAccount, String toAccount);

    List<Transaction> findByFromAccount(String fromAccount);
    List<Transaction> findByToAccount(String toAccount);
}

