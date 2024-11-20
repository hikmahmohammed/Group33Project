package application;

public class Book {
    // Variables
    private String title;
    private String author;
    private String subject;
    private double price;
    private String ISBN;
    private int quantity;

    // Constructor
    public Book(String title, String author, String subject, double price, String ISBN, int quantity) {
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.price = price;
        this.ISBN = ISBN;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to display book details
    public String displayBookDetails() {
        return "Title: " + title + "\nAuthor: " + author + "\nSubject: " + subject +
                "\nPrice: $" + price + "\nISBN: " + ISBN + "\nPublication Date: " +
                "\nQuantity: " + quantity;
    }

    // Method to check if the book is in stock
    public boolean isInStock() {
        return quantity > 0;
    }

    // Method to purchase a book, reducing quantity by one
    public void purchaseBook() {
        if (isInStock()) {
            quantity--;
        } else {
            System.out.println("Out of stock!");
        }
    }
}

