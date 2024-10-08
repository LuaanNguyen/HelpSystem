package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p> MyJavaFXApp </p>
 *
 * <p> Description: Main class for the entire project</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2024 </p>
 *
 * @author Luan Nguyen, Smit Devrukhkar, Gabriel Clark, Meadow Kubanski, Isabella Paschal
 *
 * @version 1.00		2024-10-05 Basic outline of the class
 *
 */

public class MyJavaFXApp extends Application {

    //Create a DB instance
    private static final DatabaseUtil dbUtil = new DatabaseUtil();

    //No database implementation yet, use arraylist to store users instead
    private List<User> users = new ArrayList<>();
    private User currentUser;


    @Override
    public void start(Stage primaryStage) {
        //Loading connection
        try {
            dbUtil.connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        primaryStage.setTitle("Help System"); //Set GUI title

        //Login scene
        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(10, 10, 10, 10));
        loginGrid.setHgap(5);
        loginGrid.setVgap(5);

        //Login components
        TextField userNameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        //Add individual login components to Login grid
        //GridPane parameters (node, column index, row index, column span, row span)
        loginGrid.add(new Label("Username: "), 0 ,0);
        loginGrid.add(userNameField, 1, 0);
        loginGrid.add(new Label("Password: "), 0, 1);
        loginGrid.add(passwordField, 1, 1);
        loginGrid.add(loginButton, 1, 2);
        loginGrid.add(registerButton, 1, 3);
        Scene loginScene = new Scene(loginGrid, 300, 200);


        //Register Scene
        GridPane registerGrid = new GridPane();
        registerGrid.setPadding(new Insets(10, 10, 10 ,10 ));
        registerGrid.setHgap(5);
        registerGrid.setVgap(5);

        //Register components
        TextField register_userNameField = new TextField();
        PasswordField register_passwordField = new PasswordField();
        PasswordField register_confirmPasswordField = new PasswordField();
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Student", "Instructor"); //Add the roles to the dropdown
        Button createAccountButton = new Button("Create Account");
        Button backToLoginButton = new Button("Back to Login");

        //Add components to the register grid
        registerGrid.add(new Label("New Username: "), 0, 0);
        registerGrid.add(register_userNameField, 1, 0);
        registerGrid.add(new Label("Password: "), 0, 1);
        registerGrid.add(register_passwordField, 1, 1);
        registerGrid.add(new Label("Confirm Password: "), 0, 2);
        registerGrid.add(register_confirmPasswordField, 1, 2);
        registerGrid.add(new Label("Select Role: "), 0, 3);
        registerGrid.add(roleComboBox, 1, 3);
        registerGrid.add(createAccountButton, 1, 4);
        registerGrid.add(backToLoginButton, 1, 5);
        Scene registerScene = new Scene(registerGrid, 300, 250);


        //switching between login scene and register scene
        registerButton.setOnAction(e -> primaryStage.setScene(registerScene));
        backToLoginButton.setOnAction(e -> primaryStage.setScene(loginScene));


        //Register Button action
        createAccountButton.setOnAction(e -> {
            String username = register_userNameField.getText();
            String password = register_passwordField.getText();
            String confirmPassword = register_confirmPasswordField.getText();
            String selectedRole = roleComboBox.getValue(); //Get the selected role from the ComboBox

            if (password.isEmpty() || username.isEmpty()) {
                System.out.println("Username or password cannot be empty!");
            } else if (!password.equals(confirmPassword)) {
                System.out.println("Password doesn't match");
            } else if (selectedRole == null || selectedRole.isEmpty()) {
                System.out.println("Selected Role is empty");
            } else {
                if (registerUser(username, password, selectedRole)) {
                    System.out.print("User registered Successfully");
                    primaryStage.setScene(loginScene);
                }
            }
        });

        //Load CSS
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());

        primaryStage.setScene(loginScene);
        //primaryStage.setScene(registerScene);
        primaryStage.show();
    }

    private boolean authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    private boolean registerUser(String username, String password, String role) {
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            System.out.println("User already exists!");
            return false;
        }
        users.add(new User(username, password, role));
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class User {
    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();

    //Default constructor: for testing
    public User() {
        this.username = "Admin";
        this.password = "123123";
        this.roles.add("Admin");
        this.roles.add("Student");
        this.roles.add("Instructor");
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.roles.add(role);
    }

    //Getter methods
    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public List<String> getRole() {
        return this.roles;
    }

    public void addRole(String role) {
        if (role.isEmpty()) {
            System.out.println("Can not add empty role!");
        }
        if (!roles.contains(role)) {
            roles.add(role);
        } else {
            System.out.println("Role already exists with user: " + this.username);
        }
    }
}
