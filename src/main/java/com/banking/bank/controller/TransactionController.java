package com.banking.bank.controller;

import com.banking.bank.model.Transaction;
import com.banking.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestParam String fromAccount,
                                        @RequestParam String toAccount,
                                        @RequestParam Double amount) {
        Transaction transaction = transactionService.transferMoney(fromAccount, toAccount, amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
