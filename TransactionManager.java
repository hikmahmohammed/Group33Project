package managers;

import application.Transaction;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<Transaction> transactions;

    public TransactionManager() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void displayTransactionHistory() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction.displayTransactionDetails());
            }
        }
    }

    public double calculateTotalSales() {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

