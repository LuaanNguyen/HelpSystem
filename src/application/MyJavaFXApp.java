package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    /** Create a DB instance to interact with the database */
    private static final DatabaseUtil dbUtil = new DatabaseUtil();

    @Override
    public void start(Stage primaryStage) {
        try { //Attempt to connect to H2 DB
            dbUtil.connectToDatabase();
            if (dbUtil.isDBEmpty()) { //If DB is empty, setting up admin user
                System.out.print("In-Memory Database is empty");
                primaryStage.setScene(createAdminSetupScene(primaryStage));
            } else { //If DB is not empty, go to login screen
                primaryStage.setScene(createLoginScene(primaryStage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Display primay stage
        primaryStage.setTitle("Welcome to ASU Help System 👋");
        primaryStage.show();
    }

    /**********
     * ADMIN SETUP COMPONENT
     * This is a Scene object (It's like Component in React)
     *
     * @param primaryStage primaryStage
     * @return admin setup scene
     */
    private Scene createAdminSetupScene(Stage primaryStage) {
        GridPane adminSetupGrid = new GridPane();
        adminSetupGrid.setPadding(new Insets(10, 10, 10, 10));
        adminSetupGrid.setHgap(5);
        adminSetupGrid.setVgap(5);

        TextField adminUserNameField = new TextField();
        PasswordField adminPasswordField = new PasswordField();
        PasswordField adminConfirmPasswordField = new PasswordField();
        Button setupAdminButton = new Button("Setup Admin");

        adminSetupGrid.add(new Label("Admin Username: "), 0, 0);
        adminSetupGrid.add(adminUserNameField, 1, 0);
        adminSetupGrid.add(new Label("Password: "), 0, 1);
        adminSetupGrid.add(adminPasswordField, 1, 1);
        adminSetupGrid.add(new Label("Confirm Password: "), 0, 2);
        adminSetupGrid.add(adminConfirmPasswordField, 1, 2);
        adminSetupGrid.add(setupAdminButton, 1, 3);

        setupAdminButton.setOnAction(e -> {
            String username = adminUserNameField.getText();
            String password = adminPasswordField.getText();
            String confirmPassword = adminConfirmPasswordField.getText();

            if (password.isEmpty() || username.isEmpty()) {
                System.out.println("Username or password cannot be empty!");
            } else if (!password.equals(confirmPassword)) {
                System.out.println("Password doesn't match");
            } else {
                try {
                    dbUtil.register(username, password, "Admin");
                    System.out.print("Admin user registered successfully");
                    primaryStage.setScene(createLoginScene(primaryStage));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene loginScene = new Scene(adminSetupGrid, 300, 200);
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("admin.css")).toExternalForm());
        return loginScene;
    }

    /**********
     * LOGIN COMPONENT
     *
     * @param primaryStage primaryStage
     * @return login scene
     */
    private Scene createLoginScene(Stage primaryStage) {
        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(10, 10, 10, 10));
        loginGrid.setHgap(5);
        loginGrid.setVgap(5);

        TextField userNameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        loginGrid.add(new Label("Username: "), 0, 0);
        loginGrid.add(userNameField, 1, 0);
        loginGrid.add(new Label("Password: "), 0, 1);
        loginGrid.add(passwordField, 1, 1);
        loginGrid.add(loginButton, 1, 2);
        loginGrid.add(registerButton, 1, 3);

        loginButton.setOnAction(e -> {
            String username = userNameField.getText();
            String password = passwordField.getText();
            try {
                if (dbUtil.login(username, password, "")) {
                    System.out.println("Login successful");
                    // Proceed to the next scene or functionality
                } else {
                    System.out.println("Invalid username or password");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        registerButton.setOnAction(e -> primaryStage.setScene(createRegisterScene(primaryStage)));

        Scene loginScene = new Scene(loginGrid, 300, 200);
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("login.css")).toExternalForm());
        return loginScene;
    }

    /**********
     * USER REGISTER COMPONENT
     *
     * @param primaryStage primaryStage
     * @return register scene
     */
    private Scene createRegisterScene(Stage primaryStage) {
        GridPane registerGrid = new GridPane();
        registerGrid.setPadding(new Insets(10, 10, 10, 10));
        registerGrid.setHgap(5);
        registerGrid.setVgap(5);

        TextField registerUserNameField = new TextField();
        PasswordField registerPasswordField = new PasswordField();
        PasswordField registerConfirmPasswordField = new PasswordField();
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Student", "Instructor");
        Button createAccountButton = new Button("Create Account");
        Button backToLoginButton = new Button("Back to Login");

        registerGrid.add(new Label("New Username: "), 0, 0);
        registerGrid.add(registerUserNameField, 1, 0);
        registerGrid.add(new Label("Password: "), 0, 1);
        registerGrid.add(registerPasswordField, 1, 1);
        registerGrid.add(new Label("Confirm Password: "), 0, 2);
        registerGrid.add(registerConfirmPasswordField, 1, 2);
        registerGrid.add(new Label("Select Role: "), 0, 3);
        registerGrid.add(roleComboBox, 1, 3);
        registerGrid.add(createAccountButton, 1, 4);
        registerGrid.add(backToLoginButton, 1, 5);

        createAccountButton.setOnAction(e -> {
            String username = registerUserNameField.getText();
            String password = registerPasswordField.getText();
            String confirmPassword = registerConfirmPasswordField.getText();
            String selectedRole = roleComboBox.getValue();

            if (password.isEmpty() || username.isEmpty()) {
                System.out.println("Username or password cannot be empty!");
            } else if (!password.equals(confirmPassword)) {
                System.out.println("Password doesn't match");
            } else if (selectedRole == null || selectedRole.isEmpty()) {
                System.out.println("Selected Role is empty");
            } else {
                try {
                    if (dbUtil.doesUserExist(username)) {
                        System.out.println("User already exists!");
                    } else {
                        dbUtil.register(username, password, selectedRole);
                        System.out.print("User registered successfully");
                        primaryStage.setScene(createLoginScene(primaryStage));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        Scene registerScene = new Scene(registerGrid, 300, 250);
        backToLoginButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));
        registerScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("register.css")).toExternalForm());
        return registerScene;
    }

    /*******************************************************************************************************/
    /*******************************************************************************************************
     * This is the method that launches the JavaFX application
     *
     * @param args This parameter holds the command line parameters.
     *
     */
    public static void main(String[] args) {
        launch(args);
    }
}


class User {
    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();

    /* Constructors */
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

    /* Getter Methods */
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


// Old functions
//    private boolean authenticate(String username, String password) {
//        for (User user : users) {
//            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
//                currentUser = user;
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean registerUser(String username, String password, String role) {
//        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
//            System.out.println("User already exists!");
//            return false;
//        }
//        users.add(new User(username, password, role));
//        return true;
//    }
