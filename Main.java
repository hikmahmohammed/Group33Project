//updated home page
package application;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.util.*;
import java.io.*;

public class Main extends Application {
	Button LogInButton1 = new Button ("Login");
	TextField userName = new TextField();
	TextField passWord = new TextField();
	VBox loginVBox = new VBox();
	BorderPane browsingPane = new BorderPane();
	BorderPane cartPane = new BorderPane();
	BorderPane sellPane = new BorderPane();
	Scene indivBookS;
	Scene sellBookS;
	//Scene browsingPageS;
	VBox  cartVBox = new VBox();
	VBox  sellVBox = new VBox();
// 	HBox  browsingHeader = new HBox();
// 	HBox searchOptions = new HBox();
	Scene CartS;
	//Book myBook;
	Stage stage1;

   
   String currentID; //changes with each log in -- CURRENT USER
   ArrayList<String> InCartISBN = new ArrayList<String>();
	
	public void start(Stage primaryStage) {
		try {
			stage1 = primaryStage;
         currentID = "ID-19192"; //PLACEHOLDER ID
			BorderPane root = new BorderPane();
			loginVBox.getChildren().addAll(userName,passWord,LogInButton1);
         //currentID = userName.getText(); //need ID through User database
			root.setCenter(loginVBox);
			//login Scene should be here
			Scene scene = new Scene(root,400,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage1.setScene(scene);
         
			LogInButton1.setOnAction(e-> {
            Scene browsingPageS = createBrowsingPage(primaryStage);
            stage1.setScene(browsingPageS);
         });
         
			//different scenes we are switching between
			sellBookS = listBook();
			CartS = createCart();
			
			stage1.show();
			//Load automatically to browsing page after login, then depending on clicks switch scenes
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

//Buyer Viewpoint -- Clicks on book in marketplace
public VBox viewIndividualBook(String ISBN)
{
   ArrayList<String> listing = getBookInfo(ISBN);
   
   VBox pane = new VBox();
   //add pic from book to VBox
   Button backButton = new Button("Back");
   HBox isbnBox = new HBox();
      Label lbIsbn = new Label("ISBN: ");
      TextField tfIsbn = new TextField(listing.get(0));
      tfIsbn.setEditable(false);
      isbnBox.getChildren().addAll(lbIsbn, tfIsbn);
   HBox titleBox = new HBox();
      Label lbTitle = new Label("Title: ");
      TextField tfTitle = new TextField(listing.get(1));
      tfTitle.setEditable(false);
      titleBox.getChildren().addAll(lbTitle, tfTitle);
   HBox subjectBox = new HBox();
      Label lbSubject = new Label("Subject: ");
      TextField tfSubject = new TextField(listing.get(3));
      tfSubject.setEditable(false);
      subjectBox.getChildren().addAll(lbSubject, tfSubject);
   HBox authorBox = new HBox();
      Label lbAuthor = new Label("Author: ");
      TextField tfAuthor = new TextField(listing.get(2));
      tfAuthor.setEditable(false);
      authorBox.getChildren().addAll(lbAuthor, tfAuthor);
   HBox conditionBox = new HBox();
      Label lbCondition = new Label("Condition: ");
      TextField tfCondition = new TextField(listing.get(4));
      tfCondition.setEditable(false);
      conditionBox.getChildren().addAll(lbCondition, tfCondition);
   HBox priceBox= new HBox();
      Label lbPrice = new Label("Price: ");
      TextField tfPrice = new TextField(listing.get(5));
      tfPrice.setEditable(false);
      priceBox.getChildren().addAll(lbPrice, tfPrice);
   HBox statusBox= new HBox();
      Label lbStatus = new Label("Status: ");
      TextField tfStatus = new TextField(listing.get(6));
      tfStatus.setEditable(false);
      statusBox.getChildren().addAll(lbStatus, tfStatus);
   HBox sellerIDBox= new HBox();
      Label lbSellerID = new Label("SellerID: ");
      TextField tfSellerID = new TextField(listing.get(7));
      tfSellerID.setEditable(false);
      sellerIDBox.getChildren().addAll(lbSellerID, tfSellerID);
      
   Button addToShoppingCart = new Button("Add to Cart");
   pane.getChildren().addAll(backButton, isbnBox, titleBox, authorBox, subjectBox, conditionBox, priceBox, statusBox, sellerIDBox, addToShoppingCart);
   
   backButton.setOnAction(k -> {
      Scene newBrowsingPage = createBrowsingPage(stage1); // Recreate the browsing page
      stage1.setScene(newBrowsingPage); // Set the new scene
   });
   
  
   //work here Noor
   addToShoppingCart.setOnAction(l -> {
	   String inCartNUMisbn = tfIsbn.getText();
	   //for my sake
	   if(InCartISBN.isEmpty()) {
		InCartISBN.add(inCartNUMisbn);  
	   }
	   else {
		   int found = 0;
		   for(int i = 0; i < InCartISBN.size();++i) {
			   if(inCartNUMisbn.equals((InCartISBN).get(i))) {
				   found = 1;
			   }
		   }
		   if(found == 0) {
				InCartISBN.add(inCartNUMisbn);
		   }
	   }

	   Scene newBrowsingPage = createBrowsingPage(stage1); // Recreate the browsing page
	   stage1.setScene(newBrowsingPage); // Set the new scene
  
   });
   
   return pane;
}
      
	
	//scene for browsing page, all books here, should be able to sort through them
	Scene createBrowsingPage(Stage primaryStage) {
      BorderPane browsingPane = new BorderPane();
      VBox bookCollection = new VBox();
      bookCollection.setSpacing(10);
      bookCollection.getChildren().clear();
      VBox listOfMerchHere = new VBox();
      listOfMerchHere.getChildren().clear();
      HBox  browsingHeader = new HBox();
      browsingHeader.getChildren().clear();
	   HBox searchOptions = new HBox();
      searchOptions.getChildren().clear();
      
      File folder = new File(System.getProperty("user.dir") + "/market_listings");

      File[] array = folder.listFiles(file -> file.exists() && file.isFile() && file.getName().startsWith("Listing_"));
      ArrayList<File> setOfBooks;

      if (array != null)
         setOfBooks = new ArrayList<>(Arrays.asList(array));
      else
         setOfBooks = new ArrayList<>();
      
      if (setOfBooks.isEmpty())
      {
         System.out.print("empty");
      }
      else
      {
         for(File file : setOfBooks)
         {
            String info;
            String[] splitUpInfo = new String[10];
            try (BufferedReader br = new BufferedReader(new FileReader(file)))
            {
               String infoLine;
               while ((infoLine = br.readLine()) != null)
               {
                  splitUpInfo = infoLine.split("\\|");
               }
            }
            catch(IOException q) 
            {
            //exception
            }
            String ISBN = splitUpInfo[0]; //gets isbn;
            ArrayList<String> currentListing = getBookInfo(ISBN);
            //System.out.println(ISBN);
            String price = currentListing.get(5)+".00";
            Button clickBookButton = new Button("$" + price); //PRICE AS BUTTON
            HBox completeListing = new HBox();
            completeListing.setSpacing(10);
            VBox informationListing = new VBox();
            Label name = new Label("Book Name: " + currentListing.get(1));
            Label author = new Label("Author: " + currentListing.get(2));
            Label condition = new Label("Condition: " + currentListing.get(4));
            informationListing.getChildren().addAll(name, author, condition);//book name,author,condition
            completeListing.getChildren().addAll(informationListing, clickBookButton); //ADD A PICTURE AND A VBOX OF NAME/AUTHOR/ISBN
            bookCollection.getChildren().add(completeListing);
            
            clickBookButton.setOnAction(p -> {
               String isbn = currentListing.get(0);
               //String sellerid = currentListing.get(7);
               VBox viewBookPane = viewIndividualBook(isbn);//string, string
               Scene viewBookScene = new Scene(viewBookPane, 700,400);
               primaryStage.setScene(viewBookScene);
               //mainStage.show();
            }); 
         }
      }
      
      //need a drop-down menu and some sorting and searching options, along with corresponding methods(alphabetically, condition, ect)

      Label merchHere = new Label("List of Books" + "/n"+ "should be here");
		Label search = new Label("Search Options Should Go Here");
		Button sellB = new Button ("Click to Sell Book");
		Button cartB = new Button ("Cart");
		searchOptions.getChildren().addAll(search);
		browsingHeader.getChildren().addAll(sellB,cartB);
		listOfMerchHere.getChildren().addAll(searchOptions, bookCollection);
		
		browsingPane.setCenter(listOfMerchHere);
		browsingPane.setTop(browsingHeader);
		
      Scene browsingScene = new Scene(browsingPane,400,400);
		
		sellB.setOnAction(e-> {
         Scene sellBooks = listBook();
         stage1.setScene(sellBooks);
      });
		cartB.setOnAction(e-> {
			Scene newCartPage = createCart();//recreates the browsing page
	        stage1.setScene(newCartPage);
      });

		return browsingScene;
	}
   
   public boolean listingExists(String string) 
   {
      boolean exists = string.matches("Listing_\\d+\\.txt");
      return exists;
   }
	
	
   
   //scene for shopping cart, accessed via shopping cart button
	Scene createCart() {
		//go through the InCartISBN until index is null, match with ISBN of setOfBooks, if match display book, otherwise do not
		Integer totalCost = 0;
		Button goBack = new Button ("Back");
		Button completePurchase = new Button ("Complete Purchase");
		Label cartLabel = new Label ("Shopping Cart Page");
		VBox currentCart = new VBox();//going to create a VBox with all items currently in Cart
		
		//iterate through InCartISBN and match to setOfBooks
		for (int i = 0; i < InCartISBN.size();++i){
			
			Button removeButton = new Button("Remove");
			ArrayList<String> myListing = getBookInfo(InCartISBN.get(i));
			Label imageV1 = new Label("Image Here");
			
			Label titleV2 = new Label("Title : " + myListing.get(1));
			Label conditionV2 = new Label("Condition : " + myListing.get(4));
			Label categoryV2 = new Label("Category : " + myListing.get(3));
			
			Label priceV3 = new Label("Price : " + myListing.get(5));
			Integer thisCost =Integer.valueOf(myListing.get(5));
			
			totalCost += thisCost;
			
			VBox v1 = new VBox();
		    v1.setPadding(new Insets(10,10,10,10));
			v1.getChildren().addAll(imageV1);
			VBox v2 = new VBox();
			v2.setPadding(new Insets(10,10,10,10));
			v2.getChildren().addAll(titleV2, conditionV2, categoryV2);
			VBox v3 = new VBox();
			v3.setPadding(new Insets(10,10,10,10));
			v3.getChildren().addAll(priceV3, removeButton);
			
			
			//Label thisLabel = new Label(InCartISBN.get(i));
			HBox details = new HBox();
		    details.setPadding(new Insets(10,10,10,10));
			details.getChildren().addAll(v1,v2,v3);
			currentCart.getChildren().add(details);
			String currentISBN = InCartISBN.get(i);
	
			removeButton.setOnAction(p -> {
				int theInt = getPosition(currentISBN);
				InCartISBN.remove(theInt);
				Scene newCartAfterRemoval = createCart();
				stage1.setScene(newCartAfterRemoval);
				
				//details here
				// another occurance of this scene I believe recursion yuck
		  });

		}
		BorderPane newPane = new BorderPane();
		Scene newCart = new Scene(newPane,400,400);
		HBox buttonJunk = new HBox();
		String cartTotal = Integer.toString(totalCost);
		Label cartTotalLable = new Label ("Total : $" + cartTotal);
		buttonJunk.setPadding(new Insets(10,10,10,10));
		buttonJunk.getChildren().addAll(completePurchase,goBack,cartTotalLable);
		newPane.setCenter(currentCart);
		newPane.setBottom(buttonJunk);
		
		goBack.setOnAction(e -> {
         Scene newBrowsingPage = createBrowsingPage(stage1);//recreates the browsing page
         stage1.setScene(newBrowsingPage); 
      });
		
		completePurchase.setOnAction(e->{
			InCartISBN.clear();
			Scene newCartAfterRemoval = createCart();
			stage1.setScene(newCartAfterRemoval);
			Label purchaseComplete = new Label ("Purchase Complete, Thank You");
			currentCart.getChildren().addAll(purchaseComplete);
			
			//remove books from fileSystem as well and update browsingPage
		});
		return newCart;
		
		//viewCart()
	}
   
   public ArrayList<String> getBookInfo(String ISBN)
   {
   
   //listing.get(0) = isbn 
   //listing.get(1) = title
   //listing.get(2) = author
   //listing.get(3) = subject
   //listing.get(4) = condition
   //listing.get(5) = price
   //listing.get(6) = isActive
   //listing.get(7) = sellerID
   //listing.get(8) = picture //NOT ADDED YET
   
      ArrayList<String> listing = new ArrayList<>(); //LIST OF SINGULAR BOOK INFO
      File listingPath = new File(System.getProperty("user.dir") + "/market_listings/Listing_"+ ISBN +".txt"); 
      
      if (listingPath.exists())
      {
         try (BufferedReader br = new BufferedReader(new FileReader(listingPath)))
         {
            String infoLine = "";
            while ((infoLine = br.readLine()) != null)
            {
               String[] array = infoLine.split("\\|");
               listing = new ArrayList<>(Arrays.asList(array));
            }
         }
         catch (IOException yomama)
         {
         //catch
         }
      }
      else
      {
         System.out.print("Book doesn't exist yet");
      }
      return listing;
   }
	
	//Seller Viewpoint-- Upload book info
   public Scene listBook()
   {
      VBox pane = new VBox();
      Button backButton = new Button("Back");
      //add pic from book to VBox using Image View
      HBox isbnBox = new HBox();
         Label lbIsbn = new Label("ISBN: ");
         TextField tfIsbn = new TextField();
         isbnBox.getChildren().addAll(lbIsbn, tfIsbn);
      HBox titleBox = new HBox();
         Label lbTitle = new Label("Title: ");
         TextField tfTitle = new TextField();
         titleBox.getChildren().addAll(lbTitle, tfTitle);
      HBox authorBox = new HBox();
         Label lbAuthor = new Label("Author: ");
         TextField tfAuthor = new TextField();
         authorBox.getChildren().addAll(lbAuthor, tfAuthor);
      HBox subjectBox = new HBox();
         Label lbSubject = new Label("Subject: ");
         TextField tfSubject = new TextField();
         subjectBox.getChildren().addAll(lbSubject, tfSubject);
      HBox conditionBox = new HBox();
         Label lbCondition = new Label("Condition: ");
         TextField tfCondition = new TextField();
         conditionBox.getChildren().addAll(lbCondition, tfCondition);
      HBox priceBox= new HBox();
         Label lbPrice = new Label("Price: $");
         TextField tfPrice = new TextField();
         priceBox.getChildren().addAll(lbPrice, tfPrice);
      Button uploadBook = new Button("Upload Listing");
      pane.getChildren().addAll(backButton, isbnBox, titleBox, authorBox, subjectBox, conditionBox, priceBox, uploadBook);
      
      backButton.setOnAction(k -> {
         Scene newBrowsingPage = createBrowsingPage(stage1);//recreates the browsing page
         stage1.setScene(newBrowsingPage); 
      });

      uploadBook.setOnAction(l -> {
         String id = currentID;
         Listing newListing = new Listing(tfTitle.getText(), tfAuthor.getText(), tfSubject.getText(), tfCondition.getText(), tfPrice.getText(), tfIsbn.getText(), "true", currentID);
         String condensedMaterial = newListing.fileString();
         NewListingCreator uploadListing = new NewListingCreator(tfIsbn.getText(), condensedMaterial);//ISBN , book information
         Scene newBrowsingPage = createBrowsingPage(stage1);//recreates the browsing page
         stage1.setScene(newBrowsingPage); 
         
      });
      
      return new Scene(pane, 700,700);
   }
   public int getPosition(String positionOfISBN) {
	   int t = 0;
	   int found = 0;
	  
	   while(found == 0) {
		   if(positionOfISBN.equals(InCartISBN.get(t))){
			   found = 1;
		   }
		   else {
			   found = 0;
			   ++t;
			   
		   }
	   }
	   return t;
   }

// GOES IN marketplaceUI:
// 
//    clickBookButton.setOnAction(p -> {
//       VBox viewBookPane = viewIndividualBook();
//       Scene viewBookScene = new Scene(viewBookPane, 700,400);
//       mainStage.setScene(viewBookScene);
//       mainStage.show();
//    }); 
	
	public static void main(String[] args) {
		launch(args);
	}
}
