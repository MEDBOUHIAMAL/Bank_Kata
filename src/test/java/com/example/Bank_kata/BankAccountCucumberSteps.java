package com.example.Bank_kata;

import com.example.Bank_kata.Account.Account;
import com.example.Bank_kata.Printer.StatementPrinter;
import com.example.Bank_kata.Transaction.Transaction;
import com.example.Bank_kata.Transaction.TransactionRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class BankAccountCucumberSteps {

    private TransactionRepository mockRepository;
    private StatementPrinter mockPrinter;
    private Account account;
    private List<Transaction> transactions;

    @Given("a client makes a deposit of {int} on {int}-{int}-{int}")
    public void a_client_makes_a_deposit_of_on(Integer amount, Integer day, Integer month, Integer year) {
        if (mockRepository == null) {
            mockRepository = mock(TransactionRepository.class);
            mockPrinter = mock(StatementPrinter.class);
            account = new Account(mockRepository, mockPrinter);
            transactions = new ArrayList<>();
        }

        account.deposit(amount);
        transactions.add(new Transaction(LocalDate.of(year, month, day), amount, account.getBalance()));
    }

    @And("a client withdraws {int} on {int}-{int}-{int}")
    public void a_withdrawal_of_on(Integer amount, Integer day, Integer month, Integer year) {
        account.withdraw(amount);
        transactions.add(new Transaction(LocalDate.of(year, month, day), -amount, account.getBalance()));
    }

    @When("they print their bank statement")
    public void they_print_their_bank_statement() {
        when(mockRepository.findAll()).thenReturn(transactions);

        account.printStatement();

        verify(mockPrinter).print(transactions);
    }

    @Then("they would see:")
    public void they_would_see(DataTable expectedTable) {
        List<List<String>> expectedRows = expectedTable.asLists(String.class);

        for (int i = 1; i < expectedRows.size(); i++) { // Ignorer la première ligne (en-tête)
            List<String> expectedRow = expectedRows.get(i);
            Transaction actualTransaction = transactions.get(transactions.size() - i); // Inverser l'ordre des transactions (du plus récent au plus ancien)

            String expectedDate = expectedRow.get(0);
            String expectedAmount = expectedRow.get(1);
            String expectedBalance = expectedRow.get(2);

            assert actualTransaction.getDate().toString().equals(LocalDate.parse(expectedDate, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString())
                    : "Date mismatch";

            assert Integer.toString(actualTransaction.getAmount()).equals(expectedAmount)
                    : "Amount mismatch";

            assert Integer.toString(actualTransaction.getBalance()).equals(expectedBalance)
                    : "Balance mismatch";
        }
    }
}
