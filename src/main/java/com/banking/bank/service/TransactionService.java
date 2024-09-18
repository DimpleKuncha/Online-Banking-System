package com.banking.bank.service;

import com.banking.bank.exceptions.AccountNotFoundException;
import com.banking.bank.exceptions.InsufficientFundsException;
import com.banking.bank.model.Account;
import com.banking.bank.model.Transaction;
import com.banking.bank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Transactional
    public Transaction transferMoney(String fromAccount, String toAccount, Double amount) {
            Account fromAcc = accountService.getAccountByNumber(fromAccount);
            Account toAcc = accountService.getAccountByNumber(toAccount);

            if (fromAcc == null || toAcc == null) {
                throw new AccountNotFoundException("One or both accounts not found");
            }

            if (fromAcc.getBalance() < amount) {
                throw new InsufficientFundsException("Insufficient funds in source account");
            }

            fromAcc.setBalance(fromAcc.getBalance() - amount);
            toAcc.setBalance(toAcc.getBalance() + amount);

            accountService.updateBalance(fromAcc, -amount);
            accountService.updateBalance(toAcc, amount);

            Transaction transaction = new Transaction();
            transaction.setFromAccount(fromAccount);
            transaction.setToAccount(toAccount);
            transaction.setAmount(amount);
            transaction.setTransactionDate(LocalDateTime.now());

            return transactionRepository.save(transaction);

    }
}
