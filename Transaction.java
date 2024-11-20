package application;

import java.util.Date;

public class Transaction {
    // Variables
    private User buyer;
    private User seller;
    private Book book;
    private Date transactionDate;
    private double amount;

    // Constructor
    public Transaction(User buyer, User seller, Book book, Date transactionDate, double amount) {
        this.buyer = buyer;
        this.seller = seller;
        this.book = book;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    // Gets and Sets
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // display transaction details
    public String displayTransactionDetails() {
        return "Transaction Date: " + transactionDate + "\nBuyer: " + buyer.getUsername() +
               "\nSeller: " + seller.getUsername() + "\nBook: " + book.getTitle() +
               "\nAmount: $" + amount;
    }
}

