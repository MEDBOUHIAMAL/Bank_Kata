package com.example.Bank_kata.Account;


import com.example.Bank_kata.Printer.StatementPrinter;
import com.example.Bank_kata.Transaction.Transaction;
import com.example.Bank_kata.Transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class Account implements AccountService {

    private final TransactionRepository transactionRepository;
    private int balance = 0;
    private final StatementPrinter statementPrinter;

    @Autowired
    public Account(TransactionRepository transactionRepository, StatementPrinter statementPrinter) {
        this.transactionRepository = transactionRepository;
        this.statementPrinter = statementPrinter;
    }

    @Override
    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
        balance += amount;
        transactionRepository.save(new Transaction(LocalDate.now(), amount, balance));
    }

    @Override
    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        balance -= amount;
        transactionRepository.save(new Transaction(LocalDate.now(), -amount, balance));
    }

    @Override
    public void printStatement() {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            return;
        }
        statementPrinter.print(transactions);
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }
    public int getBalance() {
        return balance;
    }
}

