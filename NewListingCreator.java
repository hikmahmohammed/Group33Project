package application;

import java.io.*;

public class NewListingCreator
{
   public NewListingCreator(String ISBN, String info)
   {
      try
      {
      String folderPath = "market_listings";
      File folder = new File(folderPath);
      if (!folder.exists())
      {
         folder.mkdirs();
      }
      File newFile = new File(folder, "Listing_" + ISBN +".txt");
         if (!newFile.exists())
         {
            newFile.createNewFile();
         }
         try (BufferedWriter bw = new BufferedWriter(new FileWriter(newFile)))
         {
            bw.write(info);
            bw.newLine();
         }        
      }
      catch (IOException p)
      {
      System.out.println("error");
      }
   }
}