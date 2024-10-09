package application;

import application.User;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    //Global Window sizes
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_WIDTH = 500;
    public static final int H_GAP = 20;
    public static final int V_GAP = 20;

    /** Create a DB instance to interact with the database */
    private static final DatabaseUtil dbUtil = new DatabaseUtil();

    @Override
    public void start(Stage primaryStage) {
        try { //Attempt to connect to H2 DB
            dbUtil.connectToDatabase();
            if (dbUtil.isDBEmpty()) { //If DB is empty, setting up admin user
                System.out.println("In-Memory Database is empty");
                primaryStage.setScene(createAdminSetupScene(primaryStage));
            } else { //If DB is not empty, go to log in screen
                primaryStage.setScene(createLoginScene(primaryStage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Display primary stage
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
        adminSetupGrid.setHgap(20);
        adminSetupGrid.setVgap(20);
        adminSetupGrid.setAlignment(Pos.CENTER);

        TextField adminUserNameField = new TextField();
        PasswordField adminPasswordField = new PasswordField();
        PasswordField adminConfirmPasswordField = new PasswordField();
        Button setupAdminButton = new Button("Setup Admin");

        adminSetupGrid.add(new Label("Database is empty, setting up Admin "), 0, 0, 4, 1);
        adminSetupGrid.add(new Label("Admin Username: "), 0, 1);
        adminSetupGrid.add(adminUserNameField, 1, 1);
        adminSetupGrid.add(new Label("Password: "), 0, 2);
        adminSetupGrid.add(adminPasswordField, 1, 2);
        adminSetupGrid.add(new Label("Confirm Password: "), 0, 3);
        adminSetupGrid.add(adminConfirmPasswordField, 1, 3);
        adminSetupGrid.add(setupAdminButton, 1, 4);

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
                    dbUtil.register( username, password, "Admin");
                    System.out.println("Admin user registered successfully");
                    primaryStage.setScene(createLoginScene(primaryStage));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene loginScene = new Scene(adminSetupGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
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
        loginGrid.getStyleClass().add("root");

        loginGrid.setPadding(new Insets(10, 10, 10, 10));
        loginGrid.setHgap(5);
        loginGrid.setVgap(10); // Adjust as necessary to control vertical spacing
        loginGrid.setAlignment(Pos.CENTER);

        TextField userNameField = new TextField();
        userNameField.getStyleClass().add("username-field");
        userNameField.setPromptText("Enter your username");

        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setPromptText("Enter your password");

        Label errorMessage = new Label();
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login_button");
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register_button");
        Button resetDatabaseButton = new Button("Reset Database");
        resetDatabaseButton.getStyleClass().add("resetDb_button");

        HBox buttonBox = new HBox(10); // HBox with 10px spacing
        buttonBox.setAlignment(Pos.CENTER); // Center the buttons
        buttonBox.getChildren().addAll(registerButton, resetDatabaseButton); // Add buttons to HBox

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(0));
        container.getChildren().add(loginGrid);
        container.getStyleClass().add("border-container");

        Label welcomeLabel = new Label("Welcome");
        loginGrid.add(welcomeLabel, 0, 0); // Add welcome label to GridPane
        welcomeLabel.getStyleClass().add("welcome-label");
        Label usernameLabel = new Label("Username: ");
        loginGrid.add(usernameLabel, 0, 1); // Add username label to GridPane
        usernameLabel.getStyleClass().add("username-label");
        loginGrid.add(userNameField, 0, 2); // Add username field to GridPane
        loginGrid.add(new Label("Password: "), 0, 3); // Add password label to GridPane
        welcomeLabel.getStyleClass().add("password-label");

        loginGrid.add(passwordField, 0, 4);  // Add password field to GridPane
        loginGrid.add(loginButton, 0, 5, 2, 1); // Span 2 columns for proper alignment
        loginGrid.add(buttonBox, 0, 6, 2, 1); // Add HBox to GridPane, spanning 2 columns


        loginButton.setOnAction(e -> {
            String username = userNameField.getText();
            String password = passwordField.getText();
            try {
                if (dbUtil.login(username, password)) {
                    errorMessage.setText("Login successful");
                    // Proceed to the next scene or functionality
                    primaryStage.setScene(userListScene(primaryStage));
                } else {
                    errorMessage.setText("Invalid username or password");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        resetDatabaseButton.setOnAction(e -> {
            try {
                System.out.println("Database reset successfully. Going back to Admin scene...");
                dbUtil.resetDatabase();
                primaryStage.setScene(createAdminSetupScene(primaryStage));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        registerButton.setOnAction(e -> primaryStage.setScene(createRegisterScene(primaryStage)));
        Scene loginScene = new Scene(container, WINDOW_HEIGHT, WINDOW_WIDTH);
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("login.css")).toExternalForm());
        loginButton.requestFocus(); // Set focus on the login button, prevents highlight on text field
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
        registerGrid.setHgap(H_GAP);
        registerGrid.setVgap(V_GAP);
        registerGrid.setAlignment(Pos.CENTER);


        TextField registerUserNameField = new TextField();
        registerUserNameField.setPromptText("Enter a username");
        PasswordField registerPasswordField = new PasswordField();
        registerPasswordField.setPromptText("Enter a password");
        PasswordField registerConfirmPasswordField = new PasswordField();
        registerConfirmPasswordField.setPromptText("Confirm password");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.setPromptText("Select a role");
        roleComboBox.getItems().addAll("Admin", "Student", "Instructor");
        Button createAccountButton = new Button("Create Account");
        Button backToLoginButton = new Button("Back to login");
        Label errorMessageLabel = new Label();
        Label matchingErrorMessageLabel = new Label();
        Label specialErrorMessageLabel = new Label();
        Label upperErrorMessageLabel = new Label();
        Label lowerErrorMessageLabel = new Label();

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
        registerGrid.add(errorMessageLabel, 1, 6);
        registerGrid.add(matchingErrorMessageLabel, 1, 7);
        registerGrid.add(specialErrorMessageLabel, 1, 8);
        registerGrid.add(upperErrorMessageLabel, 1, 9);
        registerGrid.add(lowerErrorMessageLabel, 1, 10);


        // Add listeners to the password fields
        registerPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            checkPasswordsMatch(registerPasswordField, registerConfirmPasswordField, matchingErrorMessageLabel);
            checkPasswordsUpper(registerPasswordField, upperErrorMessageLabel);
            checkPasswordsLower(registerPasswordField, lowerErrorMessageLabel);
            checkPasswordsSpecial(registerPasswordField, specialErrorMessageLabel);
        });


        createAccountButton.setOnAction(e -> {
            String username = registerUserNameField.getText();
            String password = registerPasswordField.getText();
            String confirmPassword = registerConfirmPasswordField.getText();
            String selectedRole = roleComboBox.getValue();

            if (password.isEmpty() || username.isEmpty() ) {
                errorMessageLabel.setText("Username or password cannot be empty!");
                System.out.println("Username or password or email cannot be empty!");
            } else if (!password.matches(".*[!@#$%^&*].*")) {
                errorMessageLabel.setText("Password must contain at least 1 Special Character");
                System.out.println("Password must contain at least 1 Special Character");
            }
            else if (!password.matches(".*[a-z].*")) {
                errorMessageLabel.setText("Password must contain at least 1 Lower Case letter");
                System.out.println("Password must contain at least 1 Lower Case letter");
            }
            else if (!password.matches(".*[A-Z].*")) {
                errorMessageLabel.setText("Password must contain at least 1 Upper Case letter");
                System.out.println("Password must contain at least 1 Upper Case letter");
            }
            else if (!password.equals(confirmPassword)) {
                errorMessageLabel.setText("Password doesn't match");
                System.out.println("Password doesn't match");
            } else if (selectedRole == null || selectedRole.isEmpty()) {
                errorMessageLabel.setText("Selected Role is empty");
                System.out.println("Selected Role is empty");
            } else {
                try {
                    if (dbUtil.doesUserExist(username)) {
                        System.out.println("User already exists!");
                    } else {
                        dbUtil.register( username, password, selectedRole);
                        System.out.print("User registered successfully");
                        primaryStage.setScene(createLoginScene(primaryStage));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        Scene registerScene = new Scene(registerGrid,  WINDOW_HEIGHT ,  WINDOW_WIDTH);
        backToLoginButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));
        registerScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("register.css")).toExternalForm());
        createAccountButton.requestFocus(); // Set focus on the create account button, prevents highlight on text field
        return registerScene;
    }


    /**********
     * FINISH SETUP COMPONENT
     *
     * @param primaryStage primaryStage
     * @return finish setup component
     */
//    private Scene createFinishSetupScene(Stage primaryStage, String username) {
//        GridPane finishSetupGrid = new GridPane();
//        finishSetupGrid.setPadding(new Insets(10, 10, 10, 10));
//        finishSetupGrid.setHgap(H_GAP);
//        finishSetupGrid.setVgap(V_GAP);
//        finishSetupGrid.setAlignment(Pos.CENTER);
//
//        TextField emailField = new TextField();
//        TextField firstNameField = new TextField();
//        TextField middleNameField = new TextField();
//        TextField lastNameField = new TextField();
//        TextField preferredFirstNameField = new TextField();
//        Button finishSetupButton = new Button("Finish Setup");
//
//        finishSetupGrid.add(new Label("Email: "), 0, 0);
//        finishSetupGrid.add(emailField, 1, 0);
//        finishSetupGrid.add(new Label("First Name: "), 0, 1);
//        finishSetupGrid.add(firstNameField, 1, 1);
//        finishSetupGrid.add(new Label("Middle Name: "), 0, 2);
//        finishSetupGrid.add(middleNameField, 1, 2);
//        finishSetupGrid.add(new Label("Last Name: "), 0, 3);
//        finishSetupGrid.add(lastNameField, 1, 3);
//        finishSetupGrid.add(new Label("Preferred First Name: "), 0, 4);
//        finishSetupGrid.add(preferredFirstNameField, 1, 4);
//        finishSetupGrid.add(finishSetupButton, 1, 5);
//
//        finishSetupButton.setOnAction(e -> {
//            String email = emailField.getText();
//            String firstName = firstNameField.getText();
//            String middleName = middleNameField.getText();
//            String lastName = lastNameField.getText();
//            String preferredFirstName = preferredFirstNameField.getText();
//
//            if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
//                System.out.println("Email, First Name, and Last Name cannot be empty!");
//            } else {
//                try {
//                    //dbUtil.updateUserDetails(username, email, firstName, middleName, lastName, preferredFirstName);
//                    primaryStage.setScene(createLoginScene(primaryStage));
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//        Scene finishSetupScene = new Scene(finishSetupGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
//        finishSetupScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("finishSetup.css")).toExternalForm());
//        return finishSetupScene;
//    }



    // Method to check if passwords match and update the UI
    private void checkPasswordsMatch(PasswordField passwordField, PasswordField confirmPasswordField, Label matchingErrorMessageLabel) {
        if (!confirmPasswordField.getText().equals(passwordField.getText())) {
            matchingErrorMessageLabel.setText("Passwords do not match");
        } else {
            matchingErrorMessageLabel.setText("");
        }
    }
    private void checkPasswordsUpper(PasswordField passwordField, Label upperErrorMessageLabel) {
        if (!passwordField.getText().matches(".*[A-Z].*")) {
            upperErrorMessageLabel.setText("Password must contain at least 1 Upper Case letter");
        } else {
            upperErrorMessageLabel.setText("");
        }
    }
    private void checkPasswordsLower(PasswordField passwordField, Label lowerErrorMessageLabel) {
        if (!passwordField.getText().matches(".*[a-z].*")) {
            lowerErrorMessageLabel.setText("Password must contain at least 1 Lower Case letter");
        } else {
            lowerErrorMessageLabel.setText("");
        }
    }
    private void checkPasswordsSpecial(PasswordField passwordField, Label specialErrorMessageLabel) {
        if (!passwordField.getText().matches(".*[!@#$%^&*].*")) {
            specialErrorMessageLabel.setText("Password must contain at least 1 Special Character");
        } else {
            specialErrorMessageLabel.setText("");
        }
    }

    private Scene userListScene(Stage primaryStage) {
        GridPane userListGrid = new GridPane();
        userListGrid.setPadding(new Insets(20, 20, 20, 20));
        userListGrid.setHgap(H_GAP);
        userListGrid.setVgap(V_GAP);

        String res = dbUtil.displayUsersByUser();

        ListView<String> userListView = new ListView<>();
        String[] users = dbUtil.displayUsersByUser().split("\n");
        userListView.getItems().addAll(users);

        Button backToLoginButton = new Button("Back to Login");
        userListGrid.add(userListView, 0, 0);
        userListGrid.add(backToLoginButton, 0, 1);


        backToLoginButton.setOnAction(e -> {
            primaryStage.setScene(createLoginScene(primaryStage));
        });

        Scene userListScene = new Scene(userListGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
        return userListScene;
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


