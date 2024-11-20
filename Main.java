package ui;

import managers.TransactionManager;
import managers.UserManager;
import application.Book;
import application.User;
import application.User.UserType;

public class Main {
    public static void main(String[] args) {
        TransactionManager transactionManager = new TransactionManager();
        UserManager userManager = new UserManager();

        // Create Users
        User seller = new User("john_seller", "john@example.com", UserType.SELLER);
        User buyer = new User("jane_buyer", "jane@example.com", UserType.BUYER);
        userManager.addUser(seller);
        userManager.addUser(buyer);
        
        // Display all users
        userManager.displayAllUsers();

        // Create Book and Add to Seller's Inventory
        Book book = new Book("Java Programming", "John Doe", "Programming", 49.99, "123456789", 10);
        seller.addBookForSale(book);

        // Buyer Purchases a Book
        buyer.purchaseBook(book, seller, transactionManager);

        // Display Transaction History
        transactionManager.displayTransactionHistory();

        // Display Total Sales
        System.out.println("Total Sales: $" + transactionManager.calculateTotalSales());
    }
}

