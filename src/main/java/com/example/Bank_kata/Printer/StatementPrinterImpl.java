package com.example.Bank_kata.Printer;


import com.example.Bank_kata.Transaction.Transaction;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class StatementPrinterImpl implements StatementPrinter {
    @Override
    public void print(List<Transaction> transactions) {
        System.out.println("Date       | Amount  | Balance");
        Collections.reverse(transactions);
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        Collections.reverse(transactions);  // Revenir à l'ordre initial
    }
}

