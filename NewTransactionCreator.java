package application;

import java.io.*;

public class NewTransactionCreator
{
   public NewTransactionCreator(String ISBN, String info)
   {
      try
      {
      String folderPath = "completed_transactions";
      File folder = new File(folderPath);
      if (!folder.exists())
      {
         folder.mkdirs();
      }
      File newFile = new File(folder, ISBN +"_Transaction.txt");
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