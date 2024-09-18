package com.banking.bank.service;

import com.banking.bank.exceptions.AccountNotFoundException;
import com.banking.bank.model.Account;
import com.banking.bank.repository.AccountRepository;
import com.banking.bank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public Account saveAccount(Account account) {
        // Check if an account with the given account number already exists
        validateAccount(account);

        return accountRepository.save(account);
    }

    private void validateAccount(Account account) {
        checkIfAccountNumberExists(account);
        validateBalanceNotNull(account);
        validateBalancePositive(account);
        validateMinimumBalance(account);
    }

    private static void validateMinimumBalance(Account account) {
        if (account.getBalance() < 10000) {
            throw new IllegalArgumentException("Account balance must be at least 10,000.");
        }
    }

    private static void validateBalancePositive(Account account) {
        if (account.getBalance() < 0) {
            throw new IllegalArgumentException("Account balance cannot be negative.");
        }
    }

    private static void validateBalanceNotNull(Account account) {
        if (account.getBalance() == null) {
            throw new IllegalArgumentException("Account balance cannot be null.");
        }
    }

    private void checkIfAccountNumberExists(Account account) {
        Account existingAccount = accountRepository.findByAccountNumber(account.getAccountNumber());
        if (existingAccount != null) {
            // If it exists, throw an exception to prevent creating a duplicate
            throw new IllegalArgumentException("An account with this account number already exists.");
        }
    }

    public Account getAccountByNumber(String accountNumber) {
        logger.debug("Fetching account with number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            logger.warn("No account found for account number: {}", accountNumber);
            throw new AccountNotFoundException("Account not found for account number: " + accountNumber);
        }
        return account;
    }

    @Transactional
    public void updateBalance(Account account, double amount) {
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public void deleteAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account not found for account number: " + accountNumber);
        }
        transactionRepository.deleteByFromAccountOrToAccount(accountNumber, accountNumber);
        accountRepository.delete(account);
    }

}

