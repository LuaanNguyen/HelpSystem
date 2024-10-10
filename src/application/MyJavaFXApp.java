package application;

import application.User;
import javafx.application.Application;
import javafx.geometry.HPos;
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
 * @version 1.00
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
        loginGrid.add(errorMessage, 0, 7, 2, 1); // Span 2 columns for proper alignment
        errorMessage.getStyleClass().add("error-message");
        GridPane.setColumnSpan(errorMessage, GridPane.REMAINING);
        GridPane.setHalignment(errorMessage, HPos.CENTER);

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

                    User user = dbUtil.getUserByUsername(username);

                    // Proceed to the next scene or functionality
                    if (user.getRole().contains("Admin")) {
                        primaryStage.setScene(adminScene(primaryStage));
                    } else if (user.getRole().contains("Student")) {
                        //Set other scene depending on user role
                        if (user.getFirstName().isEmpty()  || user.getLastName().isEmpty() || user.getMiddleName().isEmpty()  ) {
                           if (!user.getUsername().isEmpty()) {
                               primaryStage.setScene(finishSetupScene(primaryStage, user.getUsername()));
                           }
                        }
                        else {
                            primaryStage.setScene(studentScene(primaryStage));
                        }
                    } else if  (user.getRole().contains("Instructor")) {
                        primaryStage.setScene(instructorScene(primaryStage));
                    } else {
                        System.out.println("Error finding a right role.");
                    }
                } else {
                    errorMessage.setText("Invalid username or password");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Reset database button, prompts user to confirm resetting database
        resetDatabaseButton.setOnAction(e -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Reset Database");
            dialog.setHeaderText("Are you sure you want to reset the database?");

            ButtonType resetButtonType = new ButtonType("Reset", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(resetButtonType, cancelButtonType);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == resetButtonType) {
                    try {
                        dbUtil.resetDatabase();
                        System.out.println("Database reset successfully. Going back to Admin scene...");
                        primaryStage.setScene(createAdminSetupScene(primaryStage));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                return null;
            });

            dialog.showAndWait();
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
        registerGrid.getStyleClass().add("register-grid");
        registerGrid.setPadding(new Insets(10, 10, 10, 10));
        registerGrid.setHgap(5);
        registerGrid.setVgap(1);
        registerGrid.setAlignment(Pos.CENTER);

        TextField registerUserNameField = new TextField();
        registerUserNameField.getStyleClass().add("username-field");
        registerUserNameField.setPromptText("Enter a username");
        PasswordField registerPasswordField = new PasswordField();
        registerPasswordField.setPromptText("Enter a password");
        PasswordField registerConfirmPasswordField = new PasswordField();
        registerConfirmPasswordField.setPromptText("Confirm password");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.setPromptText("Select a role");
        roleComboBox.getItems().addAll("Admin", "Student", "Instructor");
        roleComboBox.getStyleClass().add("role-combobox");
        Button createAccountButton = new Button("Create Account");
        createAccountButton.getStyleClass().add("create-account-button");
        Button backToLoginButton = new Button("Back to login");
        backToLoginButton.getStyleClass().add("back-to-login-button");
        VBox borderContainer = new VBox();
        borderContainer.setAlignment(Pos.CENTER);
        borderContainer.setPadding(new Insets(0));
        borderContainer.getStyleClass().add("border-container");

        // Create labels for password requirements, !!! I'm not sure how to put the Label initialization in the requirementsBox and update the text
        Label errorMessageLabel = new Label();
        Label requirementUpperCase = new Label("❌ Have one uppercase character");
        Label requirementLowerCase = new Label("❌ Have one lowercase character");
        Label requirementSpecialChar = new Label("❌ Have one special character");
        Label requirementLength = new Label("❌ Have 8 characters minimum");
        Label requirementMatches = new Label("❌ Passwords match");
        VBox requirementsBox = new VBox(4, requirementUpperCase, requirementLowerCase, requirementSpecialChar, requirementLength, requirementMatches);
        registerGrid.add(requirementsBox, 0, 6, 1, 1);

        VBox registerContainer = new VBox(5, new Label("New Username: "),
                registerUserNameField, new Label("Password: "),
                registerPasswordField, new Label("Confirm Password: "),
                registerConfirmPasswordField);

        registerGrid.add(registerContainer, 0, 5, 1, 1);
        registerGrid.add(roleComboBox, 0, 7);
        registerGrid.add(createAccountButton, 0, 8);
        registerGrid.add(backToLoginButton, 1, 8);
        registerGrid.add(errorMessageLabel, 1, 5);

        // Add listeners to both password fields to validate password and match requirements
        registerPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean hasUpperCase = newValue.matches(".*[A-Z].*");
            boolean hasLowerCase = newValue.matches(".*[a-z].*");
            boolean hasSpecialChar = newValue.matches(".*[!@#$%^&*].*");
            boolean hasMinLength = newValue.length() >= 8;
            boolean passwordMatch = newValue.equals(registerConfirmPasswordField.getText());

            // Update labels
            requirementUpperCase.setText(hasUpperCase ? "✔ Have one uppercase character" : "❌ Have one uppercase character");
            requirementLowerCase.setText(hasLowerCase ? "✔ Have one lowercase character" : "❌ Have one lowercase character");
            requirementSpecialChar.setText(hasSpecialChar ? "✔ Have one special character" : "❌ Have one special character");
            requirementLength.setText(hasMinLength ? "✔ Have 8 characters minimum" : "❌ Have 8 characters minimum");
            requirementMatches.setText(passwordMatch ? "✔ Passwords match" : "❌ Passwords match");
        });

        // Listener for the confirmation password field
        registerConfirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean passwordMatch = registerPasswordField.getText().equals(newValue);

            // Update the password match label
            requirementMatches.setText(passwordMatch ? "✔ Passwords match" : "❌ Passwords match");
        });


        createAccountButton.setOnAction(e -> {
            String username = registerUserNameField.getText();
            String password = registerPasswordField.getText();
            String confirmPassword = registerConfirmPasswordField.getText();
            String selectedRole = roleComboBox.getValue();

            if (password.isEmpty() || username.isEmpty() ) {
//                errorMessageLabel.setText("Username or password cannot be empty!");
                System.out.println("Username or password or email cannot be empty!");

            } else if (!password.matches(".*[!@#$%^&*].*")) {
//                errorMessageLabel.setText("Password must contain at least 1 Special Character");
                System.out.println("Password must contain at least 1 Special Character");
            }
            else if (!password.matches(".*[a-z].*")) {
//                errorMessageLabel.setText("Password must contain at least 1 Lower Case letter");
                System.out.println("Password must contain at least 1 Lower Case letter");
            }
            else if (!password.matches(".*[A-Z].*")) {
//                errorMessageLabel.setText("Password must contain at least 1 Upper Case letter");
                System.out.println("Password must contain at least 1 Upper Case letter");
            }
            else if (!password.equals(confirmPassword)) {
//                errorMessageLabel.setText("Password doesn't match");
                System.out.println("Password doesn't match");
            } else if (selectedRole == null || selectedRole.isEmpty()) {
//                errorMessageLabel.setText("Selected Role is empty");
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
     * ADMIN SCENE
     *
     * @param primaryStage primaryStage
     * @return admin scene after successfully login as admin
     */
    private Scene adminScene(Stage primaryStage) {
        GridPane adminGrid = new GridPane();
        adminGrid.setPadding(new Insets(20, 20, 20, 20));
        adminGrid.setHgap(10);
        adminGrid.setVgap(10);

        ListView<String> userListView = new ListView<>();
        updateUserListView(userListView);

        Button deleteUserButton = new Button("Delete User");
        Button addRoleButton = new Button("Add Role");
        Button removeRoleButton = new Button("Remove Role");
        Button logoutButton = new Button("Log Out");

        adminGrid.add(userListView, 0, 0, 2, 1);
        adminGrid.add(deleteUserButton, 0, 1);
        adminGrid.add(addRoleButton, 0, 2);
        adminGrid.add(removeRoleButton, 1, 2);
        adminGrid.add(logoutButton, 0, 3, 2, 1);

        deleteUserButton.setOnAction(e -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            dbUtil.deleteUserAccount(selectedUser);
                            updateUserListView(userListView);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });


        addRoleButton.setOnAction(e -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Role");
                dialog.setHeaderText("Add Role to User");
                dialog.setContentText("Enter role:");
                dialog.showAndWait().ifPresent(role -> {
                    try {
                        dbUtil.addRoleToUser(selectedUser, role);
                        updateUserListView(userListView);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });

        removeRoleButton.setOnAction(e -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Remove Role");
                dialog.setHeaderText("Remove Role from User");

                ComboBox<String> roleComboBox = new ComboBox<>();
                roleComboBox.getItems().addAll("Admin", "Student", "Instructor");
                roleComboBox.setPromptText("Select a role");

                dialog.getDialogPane().setContent(roleComboBox);
                ButtonType removeButtonType = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(removeButtonType, ButtonType.CANCEL);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == removeButtonType) {
                        return roleComboBox.getValue();
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(role -> {
                    if (role != null) {
                        try {
                            dbUtil.removeRoleFromUser(selectedUser, role);
                            updateUserListView(userListView);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });

        logoutButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));

        return new Scene(adminGrid, 800, 600);
    }

    private void updateUserListView(ListView<String> userListView) {
        try {
            List<User> users = dbUtil.listUserAccounts();
            userListView.getItems().clear();
            for (User user : users) {
                userListView.getItems().add(user.getUsername());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**********
     * STUDENT SCENE
     *
     * @param primaryStage primaryStage
     * @return student scene after successfully login
     */

    private Scene studentScene(Stage primaryStage) {
        GridPane studentSceneGrid = new GridPane();
        studentSceneGrid.setPadding(new Insets(20, 20, 20, 20));
        studentSceneGrid.setHgap(H_GAP);
        studentSceneGrid.setVgap(V_GAP);

        Button backToLoginButton = new Button("Back to login");
        studentSceneGrid.add(new Label("Student Scene"), 0 , 0);
        studentSceneGrid.add(backToLoginButton, 0 ,1 );

        backToLoginButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));

        return new Scene(studentSceneGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
    }

    /**********
     * INSTRUCTOR SCENE
     *
     * @param primaryStage primaryStage
     * @return instructor scene after successfully login
     */
    private Scene instructorScene(Stage primaryStage) {
        GridPane instructorSceneGrid = new GridPane();
        instructorSceneGrid.setPadding(new Insets(20, 20, 20, 20));
        instructorSceneGrid.setHgap(H_GAP);
        instructorSceneGrid.setVgap(V_GAP);

        Button backToLoginButton = new Button("Back to login");
        instructorSceneGrid.add(new Label("Instructor Scene"), 0, 0);
        instructorSceneGrid.add(backToLoginButton, 0 ,1 );

        backToLoginButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));

        return new Scene(instructorSceneGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
    }

    /**********
     * FINISH SETUP COMPONENT
     *
     * @param primaryStage primaryStage
     * @return finish setup component
     */
    private Scene finishSetupScene(Stage primaryStage, String username) {
        GridPane finishSetupGrid = new GridPane();
        finishSetupGrid.setPadding(new Insets(10, 10, 10, 10));
        finishSetupGrid.setHgap(H_GAP);
        finishSetupGrid.setVgap(V_GAP);
        finishSetupGrid.setAlignment(Pos.CENTER);

        TextField emailField = new TextField();
        TextField firstNameField = new TextField();
        TextField middleNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField preferredFirstNameField = new TextField();
        Button finishSetupButton = new Button("Finish Setup");

        finishSetupGrid.add(new Label("Finish setting up your account"), 0, 0, 4 , 1);
        finishSetupGrid.add(new Label("Email: "), 0, 1);
        finishSetupGrid.add(emailField, 1, 1);
        finishSetupGrid.add(new Label("First Name: "), 0, 2);
        finishSetupGrid.add(firstNameField, 1, 2);
        finishSetupGrid.add(new Label("Middle Name: "), 0, 3);
        finishSetupGrid.add(middleNameField, 1, 3);
        finishSetupGrid.add(new Label("Last Name: "), 0, 4);
        finishSetupGrid.add(lastNameField, 1, 4);
        finishSetupGrid.add(new Label("Preferred First Name: "), 0, 5);
        finishSetupGrid.add(preferredFirstNameField, 1, 5);
        finishSetupGrid.add(finishSetupButton, 1, 6);

        finishSetupButton.setOnAction(e -> {
            String email = emailField.getText();
            String firstName = firstNameField.getText();
            String middleName = middleNameField.getText();
            String lastName = lastNameField.getText();
            String preferredFirstName = preferredFirstNameField.getText();

            if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                System.out.println("Email, First Name, and Last Name cannot be empty!");
            } else {
                try {
                    System.out.println("User Registration Completed!");

                    dbUtil.updateUserDetails(username, email, firstName, middleName, lastName, preferredFirstName);

                    /* NEEDS WORK - navigating to either instructor, admin, or student*/
                    primaryStage.setScene(studentScene(primaryStage));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene finishSetupScene = new Scene(finishSetupGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
        //finishSetupScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("finishSetup.css")).toExternalForm());
        return finishSetupScene;
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



