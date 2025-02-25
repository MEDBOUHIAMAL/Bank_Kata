package com.example.Bank_kata.Printer;


import com.example.Bank_kata.Transaction.Transaction;

import java.util.List;

public interface StatementPrinter {
    void print(List<Transaction> transactions);
}

