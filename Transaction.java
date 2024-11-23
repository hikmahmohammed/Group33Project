package application;
import java.io.*;
import java.util.*;

public class Transaction
{
   private String bookName;
   private String bookAuthor;
   private String bookSubject;
   private String bookCondition;
   private String bookPrice;
   private String bookISBN;
   private String status;
   private String sellerID;
   private String buyerID;
   private String dateSold;
   
   public Transaction(String BOOKNAME, String BOOKAUTHOR,String BOOKSUBJECT,String BOOKCONDITION,String BOOKPRICE,String BOOKISBN,String STATUS,String SELLERID,String BUYERID,String DATESOLD)
   {
   bookName = BOOKNAME;
   bookAuthor = BOOKAUTHOR;
   bookSubject = BOOKSUBJECT;
   bookCondition = BOOKCONDITION;
   bookPrice = BOOKPRICE;
   bookISBN = BOOKISBN;
   status = STATUS; //should be unavailable
   sellerID = SELLERID;
   buyerID = BUYERID;
   dateSold = DATESOLD;
   }
   
   //produces a "Transaction" to be stored when books are purchases
   //Use arrayList when getting information from information in the database
//list.get(0) = bookName 
//list.get(1) = bookAuthor
//list.get(2) = bookSubject
//list.get(3) = bookCondition
//list.get(4) = bookPrice
//list.get(5) = bookISBN
//list.get(6) = status
//list.get(7) = sellerID
//list.get(8) = buyerID
//list.get(9) = dateSold
   public String fileString()
   {
      String toString = bookName + "|" + bookAuthor+ "|"+bookSubject+ "|"+bookCondition+ "|"+bookPrice+ "|"+bookISBN+ "|"+status+ "|"+sellerID+ "|"+buyerID+ "|"+dateSold;
      
      return toString;
   }

   public String getTitle()
   {
   return this.bookName;
   } 
   public String getAuthor()
   {
   return this.bookAuthor;
   } 
   public String getSubject()
   {
   return this.bookSubject;
   } 
   public String getCondition()
   {
   return this.bookCondition;
   } 
   public String getPrice()
   {
   return this.bookPrice;
   } 
   public String getIsbn()
   {
      return this.bookISBN;
   } 
   public String getStatus()
   {
      return this.status;
   } 
   public String getSellerID()
   {
      return this.sellerID;
   } 
   public String getBuyerID()
   {
      return this.buyerID;
   } 
   public String getDateSold()
   {
      return this.dateSold;
   } 

}