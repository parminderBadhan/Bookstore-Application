package bookstoreapp;

import bookstoreapp.entity.Book;
import bookstoreapp.entity.Customer;
import bookstoreapp.entity.Owner;
import bookstoreapp.util.FileHandler;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookStoreApp extends Application {
    private Stage primaryStage;
    private Scene loginScene, ownerStartScene, ownerBooksScene, ownerCustomersScene;
    private Scene customerStartScene, customerCostScene;
    
    private Owner owner;
    private Customer currentCustomer;
    private List<Customer> customers;
    private List<Book> books;
    
    private final String BOOKS_FILE = "books.txt";
    private final String CUSTOMERS_FILE = "customers.txt";
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadData();
        createLoginScreen();
        
        primaryStage.setTitle("Book Store Application");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> saveData());
    }
    
    private void loadData() {
        owner = new Owner();
        books = FileHandler.loadBooks(BOOKS_FILE);
        customers = FileHandler.loadCustomers(CUSTOMERS_FILE);
    }
    
    private void saveData() {
        FileHandler.saveBooks(books, BOOKS_FILE);
        FileHandler.saveCustomers(customers, CUSTOMERS_FILE);
    }
    
    private void createLoginScreen() {
        Label titleLabel = new Label("Welcome to Bookstore App");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        
        Button loginButton = new Button("Login");
        
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (owner.login(username, password)) {
                resetAllBookSelections(); 
                createOwnerStartScreen();
                primaryStage.setScene(ownerStartScene);
            } else {
                Customer customer = findCustomer(username, password);
                if (customer != null) {
                    resetAllBookSelections();
                    currentCustomer = customer;
                    createCustomerStartScreen();
                    primaryStage.setScene(customerStartScene);
                } else {
                    showAlert("Login Failed", "Invalid username or password");
                }
            }
            
            usernameField.clear();
            passwordField.clear();
        });
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
            titleLabel, usernameLabel, usernameField, 
            passwordLabel, passwordField, loginButton
        );
        
        loginScene = new Scene(layout, 400, 300);
    }
    
    private Customer findCustomer(String username, String password) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username) && 
                customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }
    
    private void createOwnerStartScreen() {
        Button booksButton = new Button("Books");
        Button customersButton = new Button("Customers");
        Button logoutButton = new Button("Logout");
        
        booksButton.setOnAction(e -> {
            createOwnerBooksScreen();
            primaryStage.setScene(ownerBooksScene);
        });
        
        customersButton.setOnAction(e -> {
            createOwnerCustomersScreen();
            primaryStage.setScene(ownerCustomersScene);
        });
        
        logoutButton.setOnAction(e -> {
        resetAllBookSelections();  // Add this line
        primaryStage.setScene(loginScene);
        });
        
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(booksButton, customersButton, logoutButton);
        
        ownerStartScene = new Scene(layout, 600, 400);
    }
    
    private void createOwnerBooksScreen() {
        // Table setup
        TableView<Book> table = new TableView<>();
        
        TableColumn<Book, String> nameColumn = new TableColumn<>("Book Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Book, Double> priceColumn = new TableColumn<>("Book Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        table.getColumns().addAll(nameColumn, priceColumn);
        table.getItems().addAll(books);
        
        // Add book controls
        Label nameLabel = new Label("Name:");
        Label priceLabel = new Label("Price:");
        
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        
        Button addButton = new Button("Add");
        
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                
                if (!name.isEmpty() && price > 0) {
                    Book book = new Book(name, price);
                    books.add(book);
                    table.getItems().add(book);
                    
                    nameField.clear();
                    priceField.clear();
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid price");
            }
        });
        
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER);
        addBox.getChildren().addAll(
            nameLabel, nameField, priceLabel, priceField, addButton
        );
        
        // Bottom controls
        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");
        
        deleteButton.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                books.remove(selected);
                table.getItems().remove(selected);
            }
        });
        
        backButton.setOnAction(e -> primaryStage.setScene(ownerStartScene));
        
        HBox bottomBox = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(deleteButton, backButton);
        
        // Main layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(table, addBox, bottomBox);
        
        ownerBooksScene = new Scene(layout, 600, 500);
    }
    
    private void createOwnerCustomersScreen() {
        // Table setup
        TableView<Customer> table = new TableView<>();
        
        TableColumn<Customer, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<Customer, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        
        TableColumn<Customer, Integer> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        
        table.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn);
        table.getItems().addAll(customers);
        
        // Add customer controls
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        
        Button addButton = new Button("Add");
         addButton.setMinWidth(80);
        
        addButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (!username.isEmpty() && !password.isEmpty()) {
                Customer customer = new Customer(username, password);
                customers.add(customer);
                table.getItems().add(customer);
                
                usernameField.clear();
                passwordField.clear();
            }
        });
        
        HBox addBox = new HBox(10);
        addBox.setAlignment(Pos.CENTER);
        addBox.getChildren().addAll(
            usernameLabel, usernameField, passwordLabel, passwordField, addButton
        );
        
        // Bottom controls
        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back");
        
        deleteButton.setOnAction(e -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                customers.remove(selected);
                table.getItems().remove(selected);
            }
        });
        
        backButton.setOnAction(e -> primaryStage.setScene(ownerStartScene));
        
        HBox bottomBox = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(deleteButton, backButton);
        
        // Main layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(table, addBox, bottomBox);
        
        ownerCustomersScene = new Scene(layout, 600, 500);
    }
    
    private void createCustomerStartScreen() {
        // Welcome message
        Label welcomeLabel = new Label(String.format(
            "Welcome %s. You have %d points. Your status is %s",
            currentCustomer.getUsername(),
            currentCustomer.getPoints(),
            currentCustomer.getState().getStatus()
        ));
        
        // Book table
        TableView<Book> table = new TableView<>();
        
        TableColumn<Book, String> nameColumn = new TableColumn<>("Book Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Book, Double> priceColumn = new TableColumn<>("Book Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Book, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
       selectColumn.setCellFactory(col -> new CheckBoxTableCell());
        
        table.getColumns().addAll(nameColumn, priceColumn, selectColumn);
        table.getItems().addAll(books);
        
        // Bottom buttons
        Button buyButton = new Button("Buy");
        Button redeemButton = new Button("Redeem points and Buy");
        Button logoutButton = new Button("Logout");
        
        buyButton.setOnAction(e -> {
            double total = calculateTotal(table, false);
            resetAllBookSelections();
            showCustomerCostScreen(total);
        });
        
        redeemButton.setOnAction(e -> {
            double total = calculateTotal(table, true);
            resetAllBookSelections();
            showCustomerCostScreen(total);
        });
        
        logoutButton.setOnAction(e -> {
        resetAllBookSelections();  // Add this line
        primaryStage.setScene(loginScene);
        });
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(buyButton, redeemButton, logoutButton);
        
        // Main layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(welcomeLabel, table, buttonBox);
        
        customerStartScene = new Scene(layout, 600, 500);
    }
    
    private double calculateTotal(TableView<Book> table, boolean redeem) {
        double total = 0;
        
        for (Book book : table.getItems()) {
            if (book.isSelected()) {
                total += book.getPrice();
            }
        }
        
        if (redeem) {
        // Calculate maximum possible discount (1 CAD per 100 points)
        double maxDiscount = currentCustomer.getPoints() / 100.0;
        
        // Apply discount (but don't let total go below 0)
        double discountToApply = Math.min(maxDiscount, total);
        total -= discountToApply;
        
        // Deduct the points used (100 points per 1 CAD discounted)
        int pointsUsed = (int)(discountToApply * 100);
        currentCustomer.setPoints(currentCustomer.getPoints() - pointsUsed);
        
        // Add points for the remaining amount after redemption
        if (total > 0) {
            currentCustomer.addPoints(total);
        }
    } else {
        // Regular purchase - just add points
        if (total > 0) {
            currentCustomer.addPoints(total);
        }
    }
    
    return total;
}
    
    private void showCustomerCostScreen(double totalCost) {
         Label costLabel = new Label(String.format("Total Cost: $%.2f", totalCost));
         Label pointsLabel = new Label(String.format(
        "Points: %d, Status: %s",
        currentCustomer.getPoints(),
        currentCustomer.getState().getStatus()
        ));
    
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
    
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(costLabel, pointsLabel, logoutButton);
    
        customerCostScene = new Scene(layout, 400, 300);
        primaryStage.setScene(customerCostScene);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void resetAllBookSelections() {
    for (Book book : books) {
        book.setSelected(false);
    }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}