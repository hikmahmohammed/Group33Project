package application;
import java.io.*;
import java.util.*;

public class User
{
   private String email;
   private String password;
   private String id;
   private boolean isUserAdmin;
   
   public User(String EMAIL, String PASSWORD, String ID, boolean ISUSERADMIN)
   {
      email = EMAIL;
      password = PASSWORD;
      id = ID;
      isUserAdmin = ISUSERADMIN;
   }

   public String fileString()
   {
    //produces a "User" to be stored
   //each file in the database will be one User
   //Use arrayList when getting information from information in the database
//list.get(0) = id 
//list.get(1) = email
//list.get(2) = password
//list.get(3) = isUserAdmin //true for admin //false for normal user

   String toString;
      if (isUserAdmin)
         toString = id + "|" + email + "|" + password + "|true";
      else
         toString = id + "|" + email + "|" + password + "|false";
      return toString;
   }
}