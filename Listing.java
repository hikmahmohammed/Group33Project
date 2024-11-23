package application;
import java.io.*;
import java.util.*;

public class Listing
{
   private String title;
   private String author;
   private String subject;
   private String condition;
   private String price;
   private String isbn;
   //private Image picture; //absolute path to a picture 
   String isActive;
   String sellerID;
   
   public Listing(String TITLE, String AUTHOR, String SUBJECT, String CONDITION, String PRICE, String ISBN, String ISACTIVE, String SELLERID)
   {
      title = TITLE;
      author = AUTHOR;
      subject = SUBJECT;
      condition = CONDITION;
      price = PRICE;
      isbn = ISBN;
      //picture = PICTURE
      isActive = ISACTIVE;
      sellerID = SELLERID;
   }
   
   //produces a "Book" to be stored
   //each line in the database will be one book
   //Use arrayList when getting information from information in the database
//list.get(0) = isbn 
//list.get(1) = title
//list.get(2) = author
//list.get(3) = subject
//list.get(4) = condition
//list.get(5) = price
//list.get(6) = isActive
//list.get(7) = sellerID

   public String fileString()
   {
      String toString = isbn + "|" + title + "|" + author + "|" + subject + "|" + condition + "|" + price + "|" + isActive + "|" + sellerID;
      
      return toString;
   }
   public String getIsbn()
   {
      return this.isbn;
   }
      public String getTitle()
   {
      return this.title;
   }
      public String getAuthor()
   {
      return this.author;
   }
      public String getSubject()
   {
      return this.subject;
   }
      public String getCondition()
   {
      return this.condition;
   }
      public String getPrice()
   {
      return this.price;
   }
      public String getIsActive()
   {
      return this.isActive;
   }
      public String getSellerid()
   {
      return this.sellerID;
   }
   
}