package application;

import java.util.ArrayList;
import java.util.List;

public class User {
    // Enum to define user roles
    public enum UserType {
        BUYER, SELLER
    }

    // Variables
    private String username;
    private String email;
    private UserType userType;
    private List<Book> booksForSale;
    private List<Book> booksPurchased;

    // Constructor
    public User(String username, String email, UserType userType) {
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.booksForSale = new ArrayList<>();
        this.booksPurchased = new ArrayList<>();
    }

    // gets and sets
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Book> getBooksForSale() {
        return booksForSale;
    }

    public List<Book> getBooksPurchased() {
        return booksPurchased;
    }

    // Add and remove books for sale
    public void addBookForSale(Book book) {
        if (userType == UserType.SELLER) {
            booksForSale.add(book);
        } else {
            System.out.println("Only sellers can add books for sale.");
        }
    }

    public void removeBookForSale(Book book) {
        booksForSale.remove(book);
    }

    // purchasing books
    public void purchaseBook(Book book, User seller, managers.TransactionManager transactionManager) {
        if (userType == UserType.BUYER && book.isInStock()) {
            booksPurchased.add(book);
            book.purchaseBook();
            application.Transaction transaction = new application.Transaction(this, seller, book, new java.util.Date(), book.getPrice());
            transactionManager.addTransaction(transaction);
        }
    }

    // display books for sale
    public void displayBooksForSale() {
        if (booksForSale.isEmpty()) {
            System.out.println("No books available for sale.");
        } else {
            System.out.println("Books for Sale:");
            for (Book book : booksForSale) {
                System.out.println(book.displayBookDetails());
            }
        }
    }

    // display purchased books
    public void displayBooksPurchased() {
        if (booksPurchased.isEmpty()) {
            System.out.println("No books purchased.");
        } else {
            System.out.println("Purchased Books:");
            for (Book book : booksPurchased) {
                System.out.println(book.displayBookDetails());
            }
        }
    }
}

