//sorry for the mess lol

//NEW SHIT:
//added images to books, added RiRi login page for functionality
//added permanent user database with User.java and NewUserCreator.java
//added dropdown box on top right for some buttons

//--------------------------------------TO-DO--------------------------------------// 
/*
1. when listing book, need to implement check to make sure everything is filled
if you don't put a pic, there needs to be a default pic that is filled or something
3. overall just error handling/input handling n shit
4. made ID generator but doesn't check if ID is already in use--potential bug but not likely
5. user database allows for duplicates--need to fix
6. UI design
*/

package application;

import javafx.application.Application;

import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.util.*;
import java.io.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.nio.file.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import javafx.collections.*;
import javafx.scene.image.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Main extends Application {
	Button LogInButton1 = new Button("Login");
	TextField userName = new TextField();
	TextField passWord = new TextField();
	VBox loginVBox = new VBox();
	BorderPane browsingPane = new BorderPane();
	BorderPane cartPane = new BorderPane();
	BorderPane sellPane = new BorderPane();
	Scene indivBookS;
	Scene sellBookS;
	VBox cartVBox = new VBox();
	VBox sellVBox = new VBox();
	Scene CartS;
	Stage stage1;
	Scene homepageScene;
	final String imagesPath = "images_folder";
	ArrayList<String> InCartISBN = new ArrayList<String>();
	private static final DecimalFormat decfor = new DecimalFormat("0.00");

	String currentID; // changes with each log in -- CURRENT USER
	boolean currentUserAdmin;// changes with each log in -- CURRENT USER PERMS

	public void start(Stage primaryStage) {
		try {
			stage1 = primaryStage;
			// Homepage
			VBox homepage = new VBox(20);
			homepage.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #5C4033;");
			Label homepageLabel = new Label("SunDevil Marketplace");
			homepageLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #F1C40F;");
			Button registerButton = new Button("Register");
			Button loginButton = new Button("Login");

			homepage.getChildren().addAll(homepageLabel, registerButton, loginButton);
			homepageScene = new Scene(homepage, 400, 300);

			// Registration Page
			VBox registrationPage = new VBox(10);
			registrationPage.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #5C4033;");
			Label registrationLabel = new Label("Create Your Account");
			registrationLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #F1C40F;");
			TextField emailField = new TextField();
			emailField.setPromptText("Email Address");
			PasswordField passwordField = new PasswordField();
			passwordField.setPromptText("Password");
			CheckBox newsletterCheckBox = new CheckBox("Receive news, updates, and deals");
			Button createAccountButton = new Button("Create Account");
			Button backToHomepageButton = new Button("Return");

			registrationPage.getChildren().addAll(registrationLabel, emailField, passwordField, newsletterCheckBox,
					createAccountButton, backToHomepageButton);
			Scene registrationScene = new Scene(registrationPage, 400, 300);

			// Login Page
			VBox loginPage = new VBox(10);
			loginPage.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #5C4033;");
			Label loginLabel = new Label("Sign In");
			loginLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #F1C40F;");
			TextField loginEmailField = new TextField();
			loginEmailField.setPromptText("Email Address");
			PasswordField loginPasswordField = new PasswordField();
			loginPasswordField.setPromptText("Password");
			CheckBox rememberMeCheckBox = new CheckBox("Remember Me");
			Button loginSubmitButton = new Button("Log In");
			Button backToHomepageFromLoginButton = new Button("Return");
			loginPage.getChildren().addAll(loginLabel, loginEmailField, loginPasswordField, rememberMeCheckBox,
					loginSubmitButton, backToHomepageFromLoginButton);
			Scene loginScene = new Scene(loginPage, 400, 300);

			// Event Handlers
			registerButton.setOnAction(e -> stage1.setScene(registrationScene));
			loginButton.setOnAction(e -> stage1.setScene(loginScene));
			backToHomepageButton.setOnAction(e -> stage1.setScene(homepageScene));
			backToHomepageFromLoginButton.setOnAction(e -> stage1.setScene(homepageScene));

			// NEED TO COVER BASE OF REPEAT USERS
			createAccountButton.setOnAction(e -> {
				String email = emailField.getText();
				String password = passwordField.getText();
				if (!email.isEmpty() && !password.isEmpty()) {
					int successful = 0;
					// userDatabase.put(email, password);
					String newID = newIDGenerator();
					User newUser = new User(email, password, newID, false); // MUST CREATE CASE FOR TRUE FOR WHEN
																			// IMPLEMENTING ADMIN
					String userInfo = newUser.fileString();
					NewUserCreator registerUser = new NewUserCreator(newID, userInfo);

					File folder1 = new File(System.getProperty("user.dir") + "/user_database");
					File[] array1 = folder1
							.listFiles(file1 -> file1.exists() && file1.isFile() && file1.getName().startsWith(newID));
					ArrayList<String> realInfo = new ArrayList<>();
					if (array1.length > 0 && array1 != null) {
						int i = 0;
						while (i < array1.length) {
							try (BufferedReader br = new BufferedReader(new FileReader(array1[i]))) {
								String infoLine = "";
								while ((infoLine = br.readLine()) != null) {
									String[] random = infoLine.split("\\|");
									realInfo = new ArrayList<>(Arrays.asList(random));
								}
								if (realInfo.get(0).equals(newID)) {
									showAlert("Success", "Account created successfully!");
									emailField.clear();
									passwordField.clear();
									primaryStage.setScene(homepageScene);
									successful = 1;
									break;
								}
							} catch (IOException f) {
							}
							i++;
						}
						if (successful == 0) {
							System.out.print("Yomama");
							showAlert("Error", "Account not created");
						}
					}
				} else {
					showAlert("Error", "Please fill in all fields.");
				}
			});

			loginSubmitButton.setOnAction(e -> {
				String email = loginEmailField.getText();
				String password = loginPasswordField.getText();
				File folder = new File(System.getProperty("user.dir") + "/user_database");

				if (folder.exists()) {
					int successful = 0;
					File[] files = folder.listFiles((file -> file.isFile() && !file.getName().equals(".DS_Store"))); // array
																														// of
																														// all
																														// files
																														// in
																														// folder
					int num = 1;

					for (File file : files) {
						// System.out.println("Round " + num);
						try (BufferedReader br = new BufferedReader(new FileReader(file))) {
							String infoLine = "";
							if ((infoLine = br.readLine()) != null) {
								String[] array = infoLine.split("\\|");
								// System.out.println("Round " +num+ ": Array Length: " + array.length);
								// System.out.println("Reading file: " + file.getName());
								// for(int i = 0; i < array.length; i++) //DEBUG
								// System.out.println(array[i]);

								if (array[1].equals(email) && array[2].equals(password)) {
									showAlert("Success", "Login successful!");
									loginEmailField.clear();
									loginPasswordField.clear();
									currentID = array[0]; // SETS CURRENT ID
									if (array[3].equals("true"))
										currentUserAdmin = true; // SAYS IF ADMIN OR NOT
									else
										currentUserAdmin = false; // SAYS IF ADMIN OR NOT

									successful = 1;
									Scene browsingPageS = createBrowsingPage(stage1);
									stage1.setScene(browsingPageS);
									break;
								}
							}
						} catch (IOException yomama) {
							// catch
						}
						num++; // DEBUGGER
					}
					if (successful == 0) {
						showAlert("Error", "Invalid email or password.");
					}
				} else {
					System.out.print("User doesn't exist yet");
				}
			});

			// set up homepage
			stage1.setTitle("SunDevil Marketplace");
			stage1.setScene(homepageScene);
			stage1.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String newIDGenerator() {
		ArrayList<Integer> numList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			numList.add(i);
		}
		Collections.shuffle(numList);
		StringBuilder realNum = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			realNum.append(numList.get(i));
		}
		String newID = realNum.toString();
		return newID;
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Buyer Viewpoint -- Clicks on book in marketplace
	public VBox viewIndividualBook(String ISBN) {
		ArrayList<String> listing = getBookInfo(ISBN);
		VBox pane = new VBox();
		File folder = new File(System.getProperty("user.dir") + "/images_folder");
		File[] array = folder.listFiles(file -> file.exists() && file.isFile() && file.getName().startsWith(ISBN));
		Image picture = new Image(array[0].toURI().toString());
		ImageView pictureView = new ImageView(picture);
		pictureView.setFitWidth(150);
		pictureView.setFitHeight(150);
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
		HBox priceBox = new HBox();
		Label lbPrice = new Label("Price: ");
		TextField tfPrice = new TextField(listing.get(5));
		tfPrice.setEditable(false);
		priceBox.getChildren().addAll(lbPrice, tfPrice);
		HBox statusBox = new HBox();
		Label lbStatus = new Label("Status: ");
		TextField tfStatus = new TextField(listing.get(6));
		tfStatus.setEditable(false);
		statusBox.getChildren().addAll(lbStatus, tfStatus);
		HBox sellerIDBox = new HBox();
		Label lbSellerID = new Label("SellerID: ");
		TextField tfSellerID = new TextField(listing.get(7));
		tfSellerID.setEditable(false);
		sellerIDBox.getChildren().addAll(lbSellerID, tfSellerID);

		Button addToShoppingCart = new Button("Add to Cart");
		pane.getChildren().addAll(backButton, pictureView, isbnBox, titleBox, authorBox, subjectBox, conditionBox,
				priceBox, statusBox, sellerIDBox, addToShoppingCart);

		backButton.setOnAction(k -> {
			Scene newBrowsingPage = createBrowsingPage(stage1); // Recreate the browsing page
			stage1.setScene(newBrowsingPage); // Set the new scene
		});

		addToShoppingCart.setOnAction(l -> {
			String inCartNUMisbn = tfIsbn.getText();
			// for my sake
			if (InCartISBN.isEmpty()) {
				InCartISBN.add(inCartNUMisbn);
			} else {
				int found = 0;
				for (int i = 0; i < InCartISBN.size(); ++i) {
					if (inCartNUMisbn.equals((InCartISBN).get(i))) {
						found = 1;
					}
				}
				if (found == 0) {
					InCartISBN.add(inCartNUMisbn);
				}
			}

			Scene newBrowsingPage = createBrowsingPage(stage1); // Recreate the browsing page
			stage1.setScene(newBrowsingPage); // Set the new scene

		});

		return pane;
	}

	// scene for browsing page, all books here, should be able to sort through them
	Scene createBrowsingPage(Stage primaryStage) {
		int colorChooser = 0;
		BorderPane browsingPane = new BorderPane();
		VBox bookCollection = new VBox();
		bookCollection.setSpacing(10);
		bookCollection.getChildren().clear();
		VBox listOfMerchHere = new VBox();
		listOfMerchHere.getChildren().clear();
		HBox browsingHeader = new HBox();
		browsingHeader.getChildren().clear();
		HBox searchOptions = new HBox();
		searchOptions.getChildren().clear();
		searchOptions.setSpacing(10);
		Button searchButton = new Button("Search");// add to searchOptions
		TextField searchBar = new TextField(); // add to searchOptions
		searchBar.setPromptText("Type Here");
		ComboBox<String> filterPrice = new ComboBox<>(); // add to searchOptions
		filterPrice.setPromptText("Filter Price");
		filterPrice.getItems().addAll("--", "Lowest to Highest($)", "Highest to Lowest($)");
		ComboBox<String> filterCondition = new ComboBox<>();// add to searchOptions
		filterCondition.setPromptText("Filter Condition");
		filterCondition.getItems().addAll("--", "Brand New", "Used- Like New", "Moderately Used", "Heavily Used");
		ComboBox<String> filterSubject = new ComboBox<>();// add to searchOptions
		filterSubject.setPromptText("Filter Subject");
		filterSubject.getItems().addAll("--", "Natural Science", "Computer", "Math", "English Language", "Novel",
				"Sci-Fi", "Other");

		File folder = new File(System.getProperty("user.dir") + "/market_listings");
		File[] array = folder
				.listFiles(file -> file.exists() && file.isFile() && file.getName().startsWith("Listing_"));
		ArrayList<File> setOfBooks; // ARRAY LIST OF FILES

		if (array != null)
			setOfBooks = new ArrayList<>(Arrays.asList(array));
		else
			setOfBooks = new ArrayList<>();

		if (setOfBooks.isEmpty()) {
			System.out.print("empty");
		} else {
			setOfBooks.sort((file1, file2) -> { // AUTOMATICALLY SORT ALPHABETICALLY
				ArrayList<String> book1 = getBookInfo(file1.getName().replace("Listing_", "").replace(".txt", ""));
				ArrayList<String> book2 = getBookInfo(file2.getName().replace("Listing_", "").replace(".txt", ""));
				String yomama1 = book1.get(1);
				String yomama2 = book2.get(1);
				return yomama1.compareToIgnoreCase(yomama2);
			});

			for (File file : setOfBooks) {
				String info;
				String[] splitUpInfo = new String[10];
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String infoLine;
					while ((infoLine = br.readLine()) != null) {
						splitUpInfo = infoLine.split("\\|");
					}
				} catch (IOException q) {
					// exception
				}
				String ISBN = splitUpInfo[0]; // gets isbn;
				// copy this noor
				File folder1 = new File(System.getProperty("user.dir") + "/images_folder");
				File[] array1 = folder1
						.listFiles(file1 -> file1.exists() && file1.isFile() && file1.getName().startsWith(ISBN));
				Image picture = new Image(array1[0].toURI().toString());
				ImageView pictureView = new ImageView(picture);
				pictureView.setFitWidth(150);
				pictureView.setFitHeight(150);
				ArrayList<String> currentListing = getBookInfo(ISBN);
				System.out.println(ISBN);
				String price = currentListing.get(5) + ".00";
				Button clickBookButton = new Button("$" + price); // PRICE AS BUTTON
				HBox completeListing = new HBox(10);
				VBox informationListing = new VBox();
				Label name = new Label("Book Name: " + currentListing.get(1));
				Label author = new Label("Author: " + currentListing.get(2));
				Label condition = new Label("Condition: " + currentListing.get(4));
				informationListing.getChildren().addAll(name, author, condition);// book name,author,condition
				Region addSomeSpace = new Region();
				HBox.setHgrow(addSomeSpace, Priority.ALWAYS);
				completeListing.getChildren().addAll(pictureView, informationListing, addSomeSpace, clickBookButton); // ADD
																														// A
																														// PICTURE
																														// AND
																														// A
																														// VBOX
																														// OF
																														// NAME/AUTHOR/ISBN

				// changes background color for each book entry
				if (colorChooser == 0) {
					completeListing.setStyle("-fx-background-color: #F0F8FF;");
					colorChooser++;
				} else {
					completeListing.setStyle("-fx-background-color: #ffffff;");
					colorChooser--;
				}
				bookCollection.getChildren().add(completeListing);

				clickBookButton.setOnAction(p -> {
					String isbn = currentListing.get(0);
					// String sellerid = currentListing.get(7);
					VBox viewBookPane = viewIndividualBook(isbn);// string, string
					Scene viewBookScene = new Scene(viewBookPane, 700, 400);
					primaryStage.setScene(viewBookScene);
					// mainStage.show();
				});
			}
		}

		searchButton.setOnAction(e -> {
			int newColorChooser = 0;
			File folder1 = new File(System.getProperty("user.dir") + "/market_listings");
			File[] array1 = folder1
					.listFiles(file -> file.exists() && file.isFile() && file.getName().startsWith("Listing_"));
			ArrayList<File> setOfBooks1 = new ArrayList<>(Arrays.asList(array1));

			String price = filterPrice.getValue();
			ArrayList<File> priceFilteredBooks = priceFilter(setOfBooks1, price);
			String condition = filterCondition.getValue();
			ArrayList<File> conditionFilteredBooks = conditionFilter(priceFilteredBooks, condition);
			String subject = filterSubject.getValue();
			ArrayList<File> subjectFilteredBooks = subjectFilter(conditionFilteredBooks, subject);
			String searchEntry = searchBar.getText();
			ArrayList<File> searchFilteredBooks = entryFilter(subjectFilteredBooks, searchEntry);

			bookCollection.getChildren().clear();

			for (File file : searchFilteredBooks) {
				String info;
				String[] splitUpInfo = new String[10];
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String infoLine;
					while ((infoLine = br.readLine()) != null) {
						splitUpInfo = infoLine.split("\\|");
					}
				} catch (IOException q) {
					// exception
				}
				String ISBN = splitUpInfo[0]; // gets isbn;
				ArrayList<String> currentListing = getBookInfo(ISBN);
				System.out.println(ISBN);
				String p = currentListing.get(5) + ".00";
				Button clickBookButton = new Button("$" + p); // PRICE AS BUTTON
				HBox completeListing = new HBox();
				completeListing.setSpacing(10);
				VBox informationListing = new VBox();
				Label name = new Label("Book Name: " + currentListing.get(1));
				Label author = new Label("Author: " + currentListing.get(2));
				Label c = new Label("Condition: " + currentListing.get(4));
				informationListing.getChildren().addAll(name, author, c);// book name,author,condition
				Region addSomeSpace = new Region();
				HBox.setHgrow(addSomeSpace, Priority.ALWAYS);
				File folder2 = new File(System.getProperty("user.dir") + "/images_folder");
				File[] array2 = folder2
						.listFiles(file2 -> file2.exists() && file2.isFile() && file2.getName().startsWith(ISBN));
				Image picture1 = new Image(array2[0].toURI().toString());
				ImageView pictureView1 = new ImageView(picture1);
				pictureView1.setFitWidth(150);
				pictureView1.setFitHeight(150);
				completeListing.getChildren().addAll(pictureView1, informationListing, addSomeSpace, clickBookButton); // ADD
																														// A
																														// PICTURE
																														// AND
																														// A
																														// VBOX
																														// OF
																														// NAME/AUTHOR/ISBN
				bookCollection.getChildren().add(completeListing);

				if (newColorChooser == 0) {
					completeListing.setStyle("-fx-background-color: #F0F8FF;");
					newColorChooser++;
				} else {
					completeListing.setStyle("-fx-background-color: #ffffff;");
					newColorChooser--;
				}
				clickBookButton.setOnAction(q -> {
					String isbn = currentListing.get(0);
					// String sellerid = currentListing.get(7);
					VBox viewBookPane = viewIndividualBook(isbn);// string, string
					Scene viewBookScene = new Scene(viewBookPane, 700, 400);
					primaryStage.setScene(viewBookScene);
					// mainStage.show();
				});
			}
		});

		Label merchHere = new Label("List of Books" + "/n" + "should be here");
		Button listNewBook = new Button("Upload Book");
		Button viewCart = new Button("Cart");
		searchOptions.getChildren().addAll(filterPrice, filterCondition, filterSubject, searchBar, searchButton);
		MenuButton dropdownMenu = new MenuButton("Account");
		MenuItem adminSwitch = new MenuItem("Switch to Admin View");
		adminSwitch.setOnAction(v -> {
			// load admin view
		});
		MenuItem viewProfile = new MenuItem("View Profile");
		viewProfile.setOnAction(v -> {
			Scene profile = displayProfile();
			stage1.setScene(profile);
		});
		MenuItem logout = new MenuItem("Logout");

		logout.setOnAction(g -> {
			currentID = "";
			currentUserAdmin = false;
			stage1.setScene(homepageScene);
		});

		if (currentUserAdmin)
			dropdownMenu.getItems().addAll(viewProfile, adminSwitch, logout);
		else
			dropdownMenu.getItems().addAll(viewProfile, logout);

		Region someSpace = new Region();
		HBox.setHgrow(someSpace, Priority.ALWAYS);
		browsingHeader.getChildren().addAll(listNewBook, someSpace, viewCart, dropdownMenu);
		listOfMerchHere.getChildren().addAll(searchOptions, bookCollection);
		ScrollPane scrollyPolly = new ScrollPane(listOfMerchHere);

		browsingPane.setCenter(scrollyPolly);
		browsingPane.setTop(browsingHeader);
		scrollyPolly.setFitToWidth(true);
		scrollyPolly.setPannable(true);
		Scene browsingScene = new Scene(browsingPane, 800, 800);

		listNewBook.setOnAction(e -> {
			Scene sellBooks = listBook();
			stage1.setScene(sellBooks);
		});

		viewCart.setOnAction(e -> {
			Scene newCartPage = createCart();// recreates the browsing page
			stage1.setScene(newCartPage);
		});

		return browsingScene;
	}

	public ArrayList<File> conditionFilter(ArrayList<File> ogList, String filterChoice) {
		// filters down the list to items that have specified condition
		// "--", "Used- Like New", "Moderately Used", "Heavily Used"
		if (filterChoice == null || filterChoice.equals("--")) // NO FILTER NEEDED RETURN LIST AS IS
		{
			return ogList;
		}
		Iterator<File> yomama = ogList.iterator();
		while (yomama.hasNext()) {
			ArrayList<String> book = getBookInfo(yomama.next().getName().replace("Listing_", "").replace(".txt", ""));
			String bookCondition = book.get(4);
			if (!bookCondition.equals(filterChoice)) {
				yomama.remove();
			}
		}
		return ogList;
	}

	public ArrayList<File> entryFilter(ArrayList<File> ogList, String searchEntry) {
		if (searchEntry.equals("")) // NO SEARCH NEEDED RETURN LIST AS IS
		{
			return ogList;
		}
		Iterator<File> yomama = ogList.iterator();
		while (yomama.hasNext()) {
			ArrayList<String> book = getBookInfo(yomama.next().getName().replace("Listing_", "").replace(".txt", ""));
			String bookName = book.get(1).toLowerCase();
			String bookAuthor = book.get(2).toLowerCase();
			if (bookName.contains(searchEntry) || bookAuthor.contains(searchEntry)) // title matches
			{
				// yay :3
			} else // no books match the given search
			{
				yomama.remove();
			}
		}
		return ogList;
	}

	public ArrayList<File> subjectFilter(ArrayList<File> ogList, String filterChoice) {
		// filters down the list to items that have specified subject
		// ("--", "Natural Science" , "Computer", "Math", "English Language", "Novel",
		// "Sci-Fi", "Other");
		if (filterChoice == null || filterChoice.equals("--")) // NO FILTER NEEDED RETURN LIST AS IS
		{
			return ogList;
		}
		Iterator<File> yomama = ogList.iterator();
		while (yomama.hasNext()) {
			ArrayList<String> book = getBookInfo(yomama.next().getName().replace("Listing_", "").replace(".txt", ""));
			String bookCondition = book.get(3);
			if (!bookCondition.equals(filterChoice)) {
				yomama.remove();
			}
		}
		return ogList;
	}

	public ArrayList<File> priceFilter(ArrayList<File> ogList, String filterChoice) {
		// filters down the list to items that have specified price
		// "--", "Lowest to Highest($)", "Highest to Lowest($)"
		if (filterChoice == null || filterChoice.equals("--")) // NO FILTER NEEDED RETURN LIST AS IS
		{
			return ogList;
		}
		if (filterChoice.equals("Lowest to Highest($)")) {
			ogList.sort((file1, file2) -> {
				ArrayList<String> book1 = getBookInfo(file1.getName().replace("Listing_", "").replace(".txt", ""));
				ArrayList<String> book2 = getBookInfo(file2.getName().replace("Listing_", "").replace(".txt", ""));
				String yomama1 = book1.get(5);
				String yomama2 = book2.get(5);
				double double1 = Double.parseDouble(yomama1);
				double double2 = Double.parseDouble(yomama2);
				return Double.compare(double1, double2);
			});
		}
		if (filterChoice.equals("Highest to Lowest($)")) // POTENTIALLY HAS ERRORS
		{
			ogList.sort((file1, file2) -> {
				ArrayList<String> book1 = getBookInfo(file1.getName().replace("Listing_", "").replace(".txt", ""));
				ArrayList<String> book2 = getBookInfo(file2.getName().replace("Listing_", "").replace(".txt", ""));
				String yomama1 = book1.get(5);
				String yomama2 = book2.get(5);
				double double1 = Double.parseDouble(yomama1);
				double double2 = Double.parseDouble(yomama2);
				return Double.compare(double1, double2);
			});
			ArrayList<File> tmp = new ArrayList<>();
			for (int i = ogList.size() - 1; i >= 0; i--) {
				tmp.add(ogList.get(i));
			}
			ogList = tmp;
		}
		return ogList;
	}

	public boolean listingExists(String string) {
		boolean exists = string.matches("Listing_\\d+\\.txt");
		return exists;
	}

	// scene for shopping cart, accessed via shopping cart button
	Scene createCart() {
		// go through the InCartISBN until index is null, match with ISBN of setOfBooks,
		// if match display book, otherwise do not
		Integer totalCost = 0;
		int shipping = 10;
		Button goBack = new Button("Back");
		Button completePurchase = new Button("Complete Purchase");
		Label cartLabel = new Label("Shopping Cart Page");
		VBox currentCart = new VBox();// going to create a VBox with all items currently in Cart

		HBox shippingDetails = new HBox();
		VBox deliveryVBox = new VBox();
		TextField address = new TextField();
		TextField email = new TextField();

		ToggleGroup optionGroup = new ToggleGroup();

		RadioButton shipOption = new RadioButton("Ship");
		RadioButton pickUpOption = new RadioButton("Pick Up");

		shipOption.setToggleGroup(optionGroup);
		pickUpOption.setToggleGroup(optionGroup);
		shipOption.setSelected(true);

		deliveryVBox.getChildren().addAll(shipOption, address, email, pickUpOption);
		shippingDetails.getChildren().addAll(deliveryVBox);

		// iterate through InCartISBN and match to setOfBooks
		for (int i = 0; i < InCartISBN.size(); ++i) {

			Button removeButton = new Button("Remove");
			ArrayList<String> myListing = getBookInfo(InCartISBN.get(i));
			Label imageV1 = new Label("Image Here");
			// want a pop up that displays lable below!!!
			Label purchaseDone = new Label("Purchase Complete, Thank You");

			String currentISBN = InCartISBN.get(i);

			File cartFolder1 = new File(System.getProperty("user.dir") + "/images_folder");
			File[] cartArray1 = cartFolder1
					.listFiles(file1 -> file1.exists() && file1.isFile() && file1.getName().startsWith(currentISBN));
			Image cartPic = new Image(cartArray1[0].toURI().toString());
			ImageView cartPicView = new ImageView(cartPic);
			cartPicView.setFitWidth(100);
			cartPicView.setFitHeight(100);

			Label titleV2 = new Label("Title : " + myListing.get(1));
			Label conditionV2 = new Label("Condition : " + myListing.get(4));
			Label categoryV2 = new Label("Category : " + myListing.get(3));

			Label priceV3 = new Label("Price : " + myListing.get(5));
			Integer thisCost = Integer.valueOf(myListing.get(5));

			totalCost += thisCost;

			VBox v1 = new VBox();
			v1.setPadding(new Insets(10, 10, 10, 10));
			v1.getChildren().addAll(cartPicView);
			VBox v2 = new VBox();
			v2.setPadding(new Insets(10, 10, 10, 10));
			v2.getChildren().addAll(titleV2, conditionV2, categoryV2);
			VBox v3 = new VBox();
			v3.setPadding(new Insets(10, 10, 10, 10));
			v3.getChildren().addAll(priceV3, removeButton);

			// Label thisLabel = new Label(InCartISBN.get(i));
			HBox details = new HBox();
			details.setPadding(new Insets(10, 10, 10, 10));
			details.getChildren().addAll(v1, v2, v3);
			currentCart.getChildren().add(details);

			removeButton.setOnAction(p -> {
				int theInt = getPosition(currentISBN);
				InCartISBN.remove(theInt);
				Scene newCartAfterRemoval = createCart();
				stage1.setScene(newCartAfterRemoval);
			});

		}
		BorderPane newPane = new BorderPane();
		Scene newCart = new Scene(newPane, 400, 400);
		HBox buttonJunk = new HBox();

		VBox cartTotalVBox = new VBox();
		int subTotal = totalCost;
		Label cartTotalLabel = new Label("SubTotal : $" + subTotal);
		double tax = totalCost * .056;
		String taxString = decfor.format(tax);
		Label taxAmount = new Label("Tax: $" + taxString);
		double totalCostWall = totalCost + tax;
		String totalCostFinalString = decfor.format(totalCostWall);

		Label totalAmount = new Label("Total: $" + totalCostFinalString);

		cartTotalVBox.setPadding(new Insets(10, 10, 10, 10));
		cartTotalVBox.getChildren().addAll(cartTotalLabel, taxAmount, totalAmount);

		buttonJunk.setPadding(new Insets(10, 10, 10, 10));
		buttonJunk.getChildren().addAll(completePurchase, goBack, cartTotalVBox);
		newPane.setCenter(currentCart);
		newPane.setBottom(buttonJunk);
		newPane.setRight(shippingDetails);

		goBack.setOnAction(e -> {
			Scene newBrowsingPage = createBrowsingPage(stage1);// recreates the browsing page
			stage1.setScene(newBrowsingPage);
		});

		completePurchase.setOnAction(e -> {
			if (shipOption.isSelected()) {
				if (InCartISBN.isEmpty() == true) {
					System.out.print("Nothing in cart");
				}
				if (address.getText().equals("")) {
					System.out.print("Please put an address");

				} else if (email.getText().equals("")) {
					System.out.print("Please put an email");
				} else {
					address.clear();
					email.clear();
					while (!InCartISBN.isEmpty()) {

						storeTransaction(InCartISBN.get(0)); // STORE TRANSACTION IN TRANSACTION DATABSE
						removeListing(InCartISBN.remove(0)); // REMOVE TRANSACTION FROM LISTING DATABASE

					}
					Scene newCartAfterRemoval = createCart();
					stage1.setScene(newCartAfterRemoval);

				}
			}

			else {
				address.clear();
				email.clear();
				while (!InCartISBN.isEmpty()) {

					removeListing(InCartISBN.remove(0));

				}

				Scene newCartAfterRemoval = createCart();
				stage1.setScene(newCartAfterRemoval);

			}
			// remove books from fileSystem as well and update browsingPage
		});
		return newCart;

		// viewCart()
	}

	public ArrayList<String> getBookInfo(String ISBN) {

		// listing.get(0) = isbn
		// listing.get(1) = title
		// listing.get(2) = author
		// listing.get(3) = subject
		// listing.get(4) = condition
		// listing.get(5) = price
		// listing.get(6) = isActive
		// listing.get(7) = sellerID
		// listing.get(8) = picture //NOT ADDED YET

		ArrayList<String> listing = new ArrayList<>(); // LIST OF SINGULAR BOOK INFO
		File listingPath = new File(System.getProperty("user.dir") + "/market_listings/Listing_" + ISBN + ".txt");

		if (listingPath.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(listingPath))) {
				String infoLine = "";
				while ((infoLine = br.readLine()) != null) {
					String[] array = infoLine.split("\\|");
					listing = new ArrayList<>(Arrays.asList(array));
				}
			} catch (IOException yomama) {
				// catch
			}
		} else {
			System.out.print("Book doesn't exist yet");
		}
		return listing;
	}

	// Seller Viewpoint-- Upload book info
	public Scene listBook() {
		VBox pane = new VBox();
		Button backButton = new Button("Back");
		// add pic from book to VBox using Image View
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
		ComboBox<String> cbSubject = new ComboBox<>();
		cbSubject.getItems().addAll("Natural Science", "Computer", "Math", "English Language", "Novel", "Sci-Fi",
				"Other");
		cbSubject.getSelectionModel().selectFirst();
		// TextField tfSubject = new TextField();
		subjectBox.getChildren().addAll(lbSubject, cbSubject);
		HBox conditionBox = new HBox();
		Label lbCondition = new Label("Condition: ");
		// TextField tfCondition = new TextField();
		ComboBox<String> cbCondition = new ComboBox<>();
		cbCondition.getItems().addAll("Brand New", "Used- Like New", "Moderately Used", "Heavily Used");
		cbCondition.getSelectionModel().selectFirst();
		conditionBox.getChildren().addAll(lbCondition, cbCondition);
		HBox priceBox = new HBox();
		Label lbPrice = new Label("Original Price: $");
		TextField tfPrice = new TextField();
		priceBox.getChildren().addAll(lbPrice, tfPrice);
		Button generatePrice = new Button("Generate New Price");
		pane.getChildren().addAll(backButton, isbnBox, titleBox, authorBox, subjectBox, conditionBox, priceBox,
				generatePrice);

		backButton.setOnAction(k -> {
			Scene newBrowsingPage = createBrowsingPage(stage1);// recreates the browsing page
			stage1.setScene(newBrowsingPage);
		});

		generatePrice.setOnAction(l -> {
			if (tfIsbn.getText().equals("") || tfIsbn.getLength() != 13) {
				System.out.print("Please input the 13 digit ISBN");
			}
			if (tfTitle.getText().equals("")) {
				System.out.print("missing fields");
			}
			if (tfAuthor.getText().equals("")) {
				System.out.print("missing fields");
			}
			if (tfPrice.getText().equals("") || tfPrice.getLength() > 10) {
				System.out.print("Please Put in a whole number within a 1-10 digit range");
			}

			Boolean isbnIsNum = isAnum(tfIsbn.getText());

			if (isbnIsNum == false) {
				System.out.print("please insert a number for ISBN");
				tfIsbn.clear();
			}

			Boolean priceIsNum = isAnum(tfPrice.getText());

			if (priceIsNum == false) {
				System.out.println("please a whole Number for Price" + priceIsNum);
				tfPrice.clear();
			}

			else {
				VBox pane1 = new VBox();
				Button backButton1 = new Button("Back");
				Button uploadPicture = new Button("Upload Picture");
				HBox isbnBox1 = new HBox();
				Label lbIsbn1 = new Label("ISBN: ");
				TextField tfIsbn1 = new TextField(tfIsbn.getText());
				tfIsbn1.setEditable(false);
				isbnBox1.getChildren().addAll(lbIsbn1, tfIsbn1);
				HBox titleBox1 = new HBox();
				Label lbTitle1 = new Label("Title: ");
				TextField tfTitle1 = new TextField(tfTitle.getText());
				tfTitle1.setEditable(false);
				titleBox1.getChildren().addAll(lbTitle1, tfTitle1);
				HBox authorBox1 = new HBox();
				Label lbAuthor1 = new Label("Author: ");
				TextField tfAuthor1 = new TextField(tfAuthor.getText());
				tfAuthor1.setEditable(false);
				authorBox1.getChildren().addAll(lbAuthor1, tfAuthor1);
				HBox subjectBox1 = new HBox();
				Label lbSubject1 = new Label("Subject: ");
				TextField tfSubject1 = new TextField(cbSubject.getValue());
				tfSubject1.setEditable(false);
				subjectBox1.getChildren().addAll(lbSubject1, tfSubject1);
				HBox conditionBox1 = new HBox();
				Label lbCondition1 = new Label("Condition: ");
				TextField tfCondition1 = new TextField(cbCondition.getValue());
				tfCondition1.setEditable(false);
				conditionBox1.getChildren().addAll(lbCondition1, tfCondition1);
				HBox priceBox1 = new HBox();
				Label lbPrice1 = new Label("Generated Price: $");
				String newPrice = generateNewPrice(tfCondition1.getText(), tfPrice.getText());
				TextField tfPrice1 = new TextField(newPrice);
				tfPrice1.setEditable(false);
				priceBox1.getChildren().addAll(lbPrice1, tfPrice1);
				Button uploadBook = new Button("Upload Listing");
				pane1.getChildren().addAll(backButton1, isbnBox1, titleBox1, authorBox1, subjectBox1, conditionBox1,
						priceBox1, uploadPicture);

				backButton1.setOnAction(k -> {
					Scene ogListingScene = listBook();// recreates the browsing page
					stage1.setScene(ogListingScene);
				});

				uploadPicture.setOnAction(d -> {
					String isbn = tfIsbn1.getText();
					boolean success = uploadImage(isbn);
					if (success) {
						File folder = new File(System.getProperty("user.dir") + "/images_folder");
						File[] array = folder
								.listFiles(file -> file.exists() && file.isFile() && file.getName().startsWith(isbn));
						Image picture = new Image(array[0].toURI().toString());
						ImageView pictureView = new ImageView(picture);
						pictureView.setFitWidth(150);
						pictureView.setFitHeight(150);
						pane1.getChildren().clear();
						pane1.getChildren().addAll(backButton1, isbnBox1, titleBox1, authorBox1, subjectBox1,
								conditionBox1, priceBox1, pictureView, uploadBook);

						// ArrayList<String> book1 =
						// getBookInfo(file1.getName().replace("Listing_","").replace(".txt", ""));
						// display pic
					} else {
						// send alert saying something went wrong
						System.out.println("pic didn't upload correctly");
					}
				});

				uploadBook.setOnAction(r -> {
					String id = currentID;
					Listing newListing = new Listing(tfTitle1.getText(), tfAuthor1.getText(), tfSubject1.getText(),
							tfCondition1.getText(), newPrice, tfIsbn1.getText(), "true", currentID);
					String condensedMaterial = newListing.fileString();
					NewListingCreator uploadListing = new NewListingCreator(tfIsbn.getText(), condensedMaterial);// ISBN
																													// ,
																													// book
																													// information
					Scene newBrowsingPage = createBrowsingPage(stage1);// recreates the browsing page
					stage1.setScene(newBrowsingPage);

				});
				Scene scene2 = new Scene(pane1, 700, 700);// recreates the browsing page
				stage1.setScene(scene2);

			}

		});

		return new Scene(pane, 700, 700);
	}

	public String generateNewPrice(String condition, String priceString) {
		// "Used- Like New", "Moderately Used", "Heavily Used"
		double newPrice = 0.0;
		double priceDouble = Double.parseDouble(priceString);
		if (condition.equals("Brand New")) {
			newPrice = priceDouble;
		}
		if (condition.equals("Used- Like New")) {
			newPrice = priceDouble * 0.85;
		} else if (condition.equals("Moderately Used")) {
			newPrice = priceDouble * 0.60;
		} else if (condition.equals("Heavily Used")) {
			newPrice = priceDouble * 0.40;
		} else {
			System.out.print("error this should not be possible. fix input handling");
		}
		String result = String.valueOf(newPrice);
		String[] resultArray = result.split("\\.");
		System.out.println(resultArray[0]);
		return resultArray[0];
	}

	// get's Position in InCartISBN of specific book
	public int getPosition(String positionOfISBN) {
		int t = 0;
		int found = 0;

		while (found == 0) {
			if (positionOfISBN.equals(InCartISBN.get(t))) {
				found = 1;
			} else {
				found = 0;
				++t;

			}
		}
		return t;
	}

	// remove Listings in cart from the filesystem
	public void removeListing(String ISBN) {
		// Should probably update transaction info of appropriate users here too
		String filePath2delete = System.getProperty("user.dir") + "/market_listings/Listing_" + ISBN + ".txt";
		File deleteMe = new File(filePath2delete);
		deleteMe.delete();
	}

	public boolean isAnum(String str2Check) {
		boolean decision = true;
		int num = 1;
		int i = 0;
		while (i < str2Check.length() && num != 0) {
			if (Character.isDigit(str2Check.charAt(i)) == true) {
				++i;
				num = 1;
			} else {
				num = 0;
			}

		}

		if (num == 1) {
			decision = true;
		} else {
			decision = false;
		}
		return decision;

	}

	public boolean uploadImage(String ISBN) {
		File imageFolder = new File(imagesPath);
		boolean success = false;

		if (!imageFolder.exists()) {
			imageFolder.mkdirs();
		}
		try {
			FileChooser fc = new FileChooser();
			File file = fc.showOpenDialog(stage1);
			File uploadedFile = new File(imageFolder, ISBN + "_" + file.getName());
			Files.copy(file.toPath(), uploadedFile.toPath());
			success = true;
		} catch (IOException b) {

		}
		return success;
	}

	public void storeTransaction(String ISBN) {
		// add file to Transactiondatabsee
		File storeTransactionPath = new File(System.getProperty("user.dir") + "/completed_transactions/" + ISBN + "_Transaction.txt");
		File folder = new File(System.getProperty("user.dir") + "/market_listings");
		File[] array = folder
				.listFiles(file -> file.exists() && file.isFile() && file.getName().startsWith("Listing_"));
		ArrayList<String> info = new ArrayList<>();
		for (File file : array) {
			String[] splitUpInfo = new String[10];
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String infoLine;
				while ((infoLine = br.readLine()) != null) {
					splitUpInfo = infoLine.split("\\|");
					if (splitUpInfo[0].equals(ISBN)) // BOOK WE MUST ADD
					{
						String bookName = splitUpInfo[1];
						String bookAuthor = splitUpInfo[2];
						String bookSubject = splitUpInfo[3];
						String bookCondition = splitUpInfo[4];
						String bookPrice = splitUpInfo[5];
						String bookISBN = splitUpInfo[0];
						String status = "Unavailable";
						String sellerID = splitUpInfo[7];
						String buyerID = currentID;
						LocalDate date = LocalDate.now();
						String dateSold = date.toString();
						Transaction newTransaction = new Transaction(bookName, bookAuthor, bookSubject, bookCondition,
								bookPrice, bookISBN, status, sellerID, buyerID, dateSold);
						String transactionInfo = newTransaction.fileString();
						NewTransactionCreator storeFile = new NewTransactionCreator(bookISBN, transactionInfo);
						break;
					}
				}
			} catch (IOException i) {
				// catch
			}
		}
	}

	public Scene displayProfile2() {
		File folder = new File(System.getProperty("user.dir") + "/user_database");
		File[] array = folder.listFiles(file -> file.exists() && file.isFile() && file.getName().endsWith("_User.txt")); // array
																															// of
																															// users
		String[] splitUpInfo = new String[10];
		for (File file : array) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String infoLine;
				while ((infoLine = br.readLine()) != null) {
					String[] newarray = infoLine.split("\\|");
					if (newarray[0].equals(currentID)) // BOOK WE MUST ADD
					{
						splitUpInfo = newarray;
					}
				}
			} catch (IOException i) {
				// catch
			}
		}
		VBox base = new VBox(10);
		Button backButton = new Button("Back");
		Label accountDisplay = new Label("Account Overview");
		Label userID = new Label("User ID: " + splitUpInfo[0]);
		Label userEMAIL = new Label("Email: " + splitUpInfo[1]);
		HBox buttonHbox = new HBox(20);
		Button viewCurrentListings = new Button("View Your Listings");
		Button viewPastTransactions = new Button("View Past Transactions");
		buttonHbox.getChildren().addAll(viewCurrentListings, viewPastTransactions);
		base.getChildren().addAll(backButton, accountDisplay, userID, userEMAIL, buttonHbox);
		Scene newScene = new Scene(base, 700, 700);
		backButton.setOnAction(k -> {
			Scene newBrowsingPage = createBrowsingPage(stage1); // Recreate the browsing page
			stage1.setScene(newBrowsingPage); // Set the new scene
		});
		ObservableList<Listing> list = FXCollections.observableArrayList();
		viewCurrentListings.setOnAction(f -> {
			TableView<Listing> moot = new TableView();
			TableColumn<Listing, String> isbn = new TableColumn<>("ISBN");
			TableColumn<Listing, String> title = new TableColumn<>("Title");
			TableColumn<Listing, String> author = new TableColumn<>("Author");
			TableColumn<Listing, String> subject = new TableColumn<>("Subject");
			TableColumn<Listing, String> condition = new TableColumn<>("Condition");
			TableColumn<Listing, String> price = new TableColumn<>("Price");
			TableColumn<Listing, String> status = new TableColumn<>("Status");
			TableColumn<Listing, String> sellerID = new TableColumn<>("SellerID");

			moot.setEditable(false);

			isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
			title.setCellValueFactory(new PropertyValueFactory<>("title"));
			author.setCellValueFactory(new PropertyValueFactory<>("author"));
			subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
			condition.setCellValueFactory(new PropertyValueFactory<>("condition"));
			price.setCellValueFactory(new PropertyValueFactory<>("price"));
			status.setCellValueFactory(new PropertyValueFactory<>("isActive"));
			sellerID.setCellValueFactory(new PropertyValueFactory<>("sellerid"));

			moot.getColumns().addAll(title, author, subject, condition, price, isbn, status, sellerID);

			ArrayList<String[]> userListings = new ArrayList<>();// each index holds an array of the information
			Scene currentListingsTable;
			File folder5 = new File(System.getProperty("user.dir") + "/market_listings");
			File[] arrayOfAllFiles = folder5
					.listFiles(file -> file.exists() && file.isFile() && file.getName().startsWith("Listing_"));
			for (File file : arrayOfAllFiles) {
				String info;
				String[] splitUpInfo1 = new String[10];
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String infoLine;
					if ((infoLine = br.readLine()) != null) {
						splitUpInfo1 = infoLine.split("\\|");
						if (splitUpInfo1[7].equals(currentID)) {
							userListings.add(splitUpInfo1);
						}
					}
				} catch (IOException q) {
					// exception
				}
			}
			VBox pane = new VBox(5);
			Button realBackButton = new Button("Back");
			realBackButton.setOnAction(k -> {
				Scene newProfileDisplay = displayProfile2(); // Recreate the browsing page
				stage1.setScene(newProfileDisplay); // Set the new scene
			});
			if (!userListings.isEmpty()) {
				for (int i = 0; i < userListings.size(); i++) // goes through each listing
				{
					String[] fileInfo = userListings.get(i); // infoLine
					System.out.println("ISBN: " + fileInfo[0]);
					list.add(new Listing(fileInfo[1], fileInfo[2], fileInfo[3], fileInfo[4], fileInfo[5], fileInfo[0],
							fileInfo[6], fileInfo[7]));
				}
				pane.setPadding(new Insets(10, 0, 0, 10));
				moot.setItems(list);
				pane.getChildren().addAll(realBackButton, moot);
				currentListingsTable = new Scene(pane, 800, 600); // Recreate the browsing page
			} else {
				Label lbl = new Label("No Current Listings Available");
				pane.getChildren().addAll(realBackButton, lbl);
				currentListingsTable = new Scene(pane, 400, 400);
			}
			stage1.setScene(currentListingsTable);
		});
		viewPastTransactions.setOnAction(v -> {
			createTable();
			// stage1.setScene(sceneWOW);
		});
		return newScene;
	}

	public Scene displayProfile() {
		File folder = new File(System.getProperty("user.dir") + "/user_database");
		File[] array = folder.listFiles(file -> file.exists() && file.isFile() && file.getName().endsWith("_User.txt")); // array
																															// of
																															// users
		String[] splitUpInfo = new String[10];
		for (File file : array) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String infoLine;
				while ((infoLine = br.readLine()) != null) {
					String[] newarray = infoLine.split("\\|");
					if (newarray[0].equals(currentID)) // BOOK WE MUST ADD
					{
						splitUpInfo = newarray;
					}
				}
			} catch (IOException i) {
				// catch
			}
		}
		VBox base = new VBox(10);
		Button backButton = new Button("Back");
		Label accountDisplay = new Label("Account Overview");
		Label userID = new Label("User ID: " + splitUpInfo[0]);
		Label userEMAIL = new Label("Email: " + splitUpInfo[1]);
		HBox buttonHbox = new HBox(20);
		Button viewCurrentListings = new Button("View Your Listings");
		Button viewPastTransactions = new Button("View Past Transactions");
		buttonHbox.getChildren().addAll(viewCurrentListings, viewPastTransactions);
		base.getChildren().addAll(backButton, accountDisplay, userID, userEMAIL, buttonHbox);
		Scene newScene = new Scene(base, 700, 700);
		backButton.setOnAction(k -> {
			Scene newBrowsingPage = createBrowsingPage(stage1); // Recreate the browsing page
			stage1.setScene(newBrowsingPage); // Set the new scene
		});

		ObservableList<Listing> list = FXCollections.observableArrayList();
		viewCurrentListings.setOnAction(f -> {
			TableView<Listing> moot = new TableView();
			TableColumn<Listing, String> isbn = new TableColumn<>("ISBN");
			TableColumn<Listing, String> title = new TableColumn<>("Title");
			TableColumn<Listing, String> author = new TableColumn<>("Author");
			TableColumn<Listing, String> subject = new TableColumn<>("Subject");
			TableColumn<Listing, String> condition = new TableColumn<>("Condition");
			TableColumn<Listing, String> price = new TableColumn<>("Price");
			TableColumn<Listing, String> status = new TableColumn<>("Status");
			TableColumn<Listing, String> sellerID = new TableColumn<>("SellerID");

			moot.setEditable(false);

			isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
			title.setCellValueFactory(new PropertyValueFactory<>("title"));
			author.setCellValueFactory(new PropertyValueFactory<>("author"));
			subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
			condition.setCellValueFactory(new PropertyValueFactory<>("condition"));
			price.setCellValueFactory(new PropertyValueFactory<>("price"));
			status.setCellValueFactory(new PropertyValueFactory<>("isActive"));
			sellerID.setCellValueFactory(new PropertyValueFactory<>("sellerid"));

			moot.getColumns().addAll(title, author, subject, condition, price, isbn, status, sellerID);

			ArrayList<String[]> userListings = new ArrayList<>();// each index holds an array of the information
			Scene currentListingsTable;
			File folder5 = new File(System.getProperty("user.dir") + "/market_listings");
			File[] arrayOfAllFiles = folder5
					.listFiles(file -> file.exists() && file.isFile() && file.getName().startsWith("Listing_"));
			for (File file : arrayOfAllFiles) {
				String info;
				String[] splitUpInfo1 = new String[10];
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					String infoLine;
					if ((infoLine = br.readLine()) != null) {
						splitUpInfo1 = infoLine.split("\\|");
						if (splitUpInfo1[7].equals(currentID)) {
							userListings.add(splitUpInfo1);
						}
					}
				} catch (IOException q) {
					// exception
				}
			}
			VBox pane = new VBox(5);
			Button realBackButton = new Button("Back");
			realBackButton.setOnAction(k -> {
				Scene newProfileDisplay = displayProfile2(); // Recreate the browsing page
				stage1.setScene(newProfileDisplay); // Set the new scene
			});
			if (!userListings.isEmpty()) {
				for (int i = 0; i < userListings.size(); i++) // goes through each listing
				{
					String[] fileInfo = userListings.get(i); // infoLine
					System.out.println("ISBN: " + fileInfo[0]);
					list.add(new Listing(fileInfo[1], fileInfo[2], fileInfo[3], fileInfo[4], fileInfo[5], fileInfo[0],
							fileInfo[6], fileInfo[7]));
				}
				pane.setPadding(new Insets(10, 0, 0, 10));
				moot.setItems(list);
				pane.getChildren().addAll(realBackButton, moot);
				currentListingsTable = new Scene(pane, 800, 600); // Recreate the browsing page
			} else {
				Label lbl = new Label("No Current Listings Available");
				pane.getChildren().addAll(realBackButton, lbl);
				currentListingsTable = new Scene(pane, 400, 400);
			}
			stage1.setScene(currentListingsTable);
		});
		viewPastTransactions.setOnAction(v -> {
			createTable();
			//stage1.setScene(sceneWOW);
		});
		return newScene;
	}

	public void createTable() {
		ObservableList<Transaction> listTransactions = FXCollections.observableArrayList();
		TableView<Transaction> moot = new TableView();
		TableColumn<Transaction, String> isbn = new TableColumn<>("ISBN");
		TableColumn<Transaction, String> title = new TableColumn<>("Title");
		TableColumn<Transaction, String> author = new TableColumn<>("Author");
		TableColumn<Transaction, String> subject = new TableColumn<>("Subject");
		TableColumn<Transaction, String> condition = new TableColumn<>("Condition");
		TableColumn<Transaction, String> price = new TableColumn<>("Price");
		TableColumn<Transaction, String> status = new TableColumn<>("Status");
		TableColumn<Transaction, String> sellerID = new TableColumn<>("SellerID");
		TableColumn<Transaction, String> buyerID = new TableColumn<>("BuyerID");
		TableColumn<Transaction, String> dateSold = new TableColumn<>("Date Sold");

		moot.setEditable(false);

		isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
		condition.setCellValueFactory(new PropertyValueFactory<>("condition"));
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		status.setCellValueFactory(new PropertyValueFactory<>("status"));
		sellerID.setCellValueFactory(new PropertyValueFactory<>("sellerID"));
		buyerID.setCellValueFactory(new PropertyValueFactory<>("buyerID"));
		dateSold.setCellValueFactory(new PropertyValueFactory<>("dateSold"));

		moot.getColumns().addAll(title, author, subject, condition, price, isbn, status, sellerID, buyerID, dateSold);

		ArrayList<String[]> userListings = new ArrayList<>();// each index holds an array of the information
		Scene currentListingsTable;
		File folder5 = new File(System.getProperty("user.dir") + "/completed_transactions");
		File[] arrayOfAllFiles = folder5
				.listFiles(file -> file.exists() && file.isFile() && file.getName().endsWith("_Transaction.txt"));
		System.out.println("Array Size: " + arrayOfAllFiles.length);
		for (File file : arrayOfAllFiles) {
			String info;
			String[] splitUpInfo1 = new String[10];
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String infoLine;
				if ((infoLine = br.readLine()) != null) {
					splitUpInfo1 = infoLine.split("\\|");
					if (splitUpInfo1[7].equals(currentID) || splitUpInfo1[8].equals(currentID)) {
						userListings.add(splitUpInfo1);
					}
				}
			} catch (IOException q) {
				// exception
			}
		}
		VBox pane = new VBox(5);
		Button realBackButton = new Button("Back");
		realBackButton.setOnAction(k -> {
			Scene newProfileDisplay = displayProfile2(); // Recreate the browsing page
			stage1.setScene(newProfileDisplay); // Set the new scene
		});
		if (!userListings.isEmpty()) {
			for (int i = 0; i < userListings.size(); i++) // goes through each listing
			{
				String[] fileInfo = userListings.get(i); // infoLine
				// System.out.println("ISBN: " + fileInfo[0]);
				listTransactions.add(new Transaction(fileInfo[0], fileInfo[1], fileInfo[2], fileInfo[3], fileInfo[4],
						fileInfo[5], fileInfo[6], fileInfo[7], fileInfo[8], fileInfo[9]));
			}
			pane.setPadding(new Insets(10, 0, 0, 10));
			moot.setItems(listTransactions);
			pane.getChildren().addAll(realBackButton, moot);
			currentListingsTable = new Scene(pane, 1000, 600); // Recreate the browsing page
		} else {
			Label lbl = new Label("No Past Transactions Available");
			pane.getChildren().addAll(realBackButton, lbl);
			currentListingsTable = new Scene(pane, 400, 400);
		}

		stage1.setScene(currentListingsTable);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
