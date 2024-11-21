//Object for each listing that is uploaded to the Marketplace
//I think storing everything as strings will be easiest -- but open to changes
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
//list.get(8) = picture //NOT ADDED YET
   public String fileString()
   {
      String toString = isbn + "|" + title + "|" + author + "|" + subject + "|" + condition + "|" + price + "|" + isActive + "|" + sellerID;// + picture;
      
      return toString;
   }
}
