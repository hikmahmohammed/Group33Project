package application;

import java.io.*;

public class NewUserCreator
{
   public NewUserCreator(String ID, String info)
   {
      try
      {
      String folderPath = "user_database";
      File folder = new File(folderPath);
      if (!folder.exists())
      {
         folder.mkdirs();
      }
      File newFile = new File(folder, ID + "_User.txt");
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