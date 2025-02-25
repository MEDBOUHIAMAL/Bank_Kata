package com.example.Bank_kata;

import com.example.Bank_kata.Account.Account;
import com.example.Bank_kata.Printer.StatementPrinter;
import com.example.Bank_kata.Transaction.Transaction;
import com.example.Bank_kata.Transaction.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


public class AccountTest {
    @Mock
    private StatementPrinter mockPrinter;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeposit() {
        account.deposit(1000);
        assertEquals(1000, account.getBalance());
    }

    @Test
    void testInvalidDeposit() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-500), "Deposit amount must be greater than 0");
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0), "Deposit amount must be greater than 0");
    }

    @Test
    void testWithdraw() {
        account.deposit(1000);
        account.withdraw(500);
        assertEquals(500, account.getBalance());
    }

    @Test
    void testInvalidWithdraw() {
        account.deposit(1000);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-500), "Withdrawal amount must be greater than 0");
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0), "Withdrawal amount must be greater than 0");
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(2000), "Insufficient balance");
    }

    @Test
    void when_Call_Printer_With_Correct_Transaction_test_should_success() {
        List<Transaction> transactions = List.of(
                new Transaction(LocalDate.now(), 1000, 1000),
                new Transaction(LocalDate.now(), 2000, 3000),
                new Transaction(LocalDate.now(), -500, 2500)
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        account.deposit(1000);
        account.deposit(2000);
        account.withdraw(500);
        account.printStatement();

        verify(mockPrinter).print(transactions);
    }

    @Test
    void when_No_Transactions_Printer_Should_Not_Be_Called() {
        account.printStatement();
        verify(mockPrinter, never()).print(anyList());
    }
}

