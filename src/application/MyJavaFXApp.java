package application;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.util.Pair;
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
import java.util.List;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;


/**
 * <p> MyJavaFXApp </p>
 *
 * <p> Description: Main class for the entire project</p>
 *
 * <p> Copyright: Lynn Robert Carter © 2024 </p>
 *
 * @author Luan Nguyen, Smit Devrukhkar, Gabriel Clark, Meadow Kubanski, Isabella Paschal
 * @version 1.00
 */

public class MyJavaFXApp extends Application {
    //Global Window sizes
    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 900;
    public static final int H_GAP = 20;
    public static final int V_GAP = 20;

    public static String currentUser = "";

    /**
     * Create a DB instance to interact with the database
     */
    private static final DatabaseUtil dbUtil;

    static {
        try {
            dbUtil = new DatabaseUtil();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try { //Attempt to connect to H2 DB
            dbUtil.connectToDatabase();
            if (dbUtil.isDBEmpty()) { //If DB is empty, setting up admin user, alert user
                System.out.println("In-Memory Database is empty");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Admin Setup");
                alert.setHeaderText("Database is empty. Please setup an admin user.");
                alert.showAndWait();
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
        adminSetupGrid.getStyleClass().add("root");

        adminSetupGrid.setPadding(new Insets(10, 10, 10, 10));
        adminSetupGrid.setHgap(5);
        adminSetupGrid.setVgap(7);
        adminSetupGrid.setAlignment(Pos.CENTER);

        // Username field styling
        TextField adminUserNameField = new TextField();
        adminUserNameField.getStyleClass().add("username-field");
        adminUserNameField.setPromptText("Enter your username");
        // Password field styling
        PasswordField adminPasswordField = new PasswordField();
        adminPasswordField.getStyleClass().add("password-field");
        adminPasswordField.setPromptText("Enter a password");

        // Confirm Password field styling
        PasswordField adminConfirmPasswordField = new PasswordField();
        adminConfirmPasswordField.getStyleClass().add("password-field");
        adminConfirmPasswordField.setPromptText("Confirm password");

        Button setupAdminButton = new Button("Setup Admin");
        setupAdminButton.getStyleClass().add("setup-admin-button");

        Label adminRegisterLabel = new Label("Admin Registration");
        adminSetupGrid.add(adminRegisterLabel, 0, 0, 4, 1);
        adminRegisterLabel.getStyleClass().add("admin-setup-label");

        Label adminUsernameLabel = new Label("Admin Username: ");
        adminSetupGrid.add(adminUsernameLabel, 0, 4);
        adminUsernameLabel.getStyleClass().add("admin-username-label");
        adminSetupGrid.add(adminUserNameField, 0, 5);

        Label adminPasswordLabel = new Label("Admin Password: ");
        adminSetupGrid.add(adminPasswordLabel, 0, 6);
        adminPasswordField.getStyleClass().add("password-field");
        adminSetupGrid.add(adminPasswordField, 0, 7);


        Label AdminErrorMessage = new Label();
        adminSetupGrid.add(AdminErrorMessage, 0, 11);
        AdminErrorMessage.getStyleClass().add("error-message");
        GridPane.setColumnSpan(AdminErrorMessage, GridPane.REMAINING);
        GridPane.setHalignment(AdminErrorMessage, HPos.CENTER);

        adminSetupGrid.add(new Label("Confirm Password: "), 0, 8);
        adminSetupGrid.add(adminConfirmPasswordField, 0, 9);
        adminConfirmPasswordField.getStyleClass().add("password-field");
        adminSetupGrid.add(setupAdminButton, 0, 10);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(0));
        container.getChildren().add(adminSetupGrid);
        container.getStyleClass().add("border-container");


        setupAdminButton.setOnAction(e -> {
            String username = adminUserNameField.getText();
            String password = adminPasswordField.getText();
            String confirmPassword = adminConfirmPasswordField.getText();

            if (password.isEmpty() || username.isEmpty()) {
                System.out.println("Username or password cannot be empty!");
                AdminErrorMessage.setText("Invalid username or password");
            } else if (!password.equals(confirmPassword)) {
                System.out.println("Password doesn't match");
                AdminErrorMessage.setText("Passwords do not match");
            } else {
                try {
                    dbUtil.register(username, password, "Admin");
                    System.out.println("Admin user registered successfully");
                    primaryStage.setScene(createLoginScene(primaryStage));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene adminSetupScene = new Scene(container, WINDOW_HEIGHT, WINDOW_WIDTH);
        adminSetupScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("adminSetup.css")).toExternalForm());
        setupAdminButton.requestFocus();
        return adminSetupScene;
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
        loginGrid.add(errorMessage, 0, 7, 2, 1);
        errorMessage.getStyleClass().add("error-message");
        GridPane.setColumnSpan(errorMessage, GridPane.REMAINING);
        GridPane.setHalignment(errorMessage, HPos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("login_button");

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register_button");

        Button testButton = new Button("");
        testButton.getStyleClass().add("test_button");

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
        loginGrid.add(testButton, 1, 6, 2, 1); // Span 2 columns for proper alignment
        //loginGrid.add(new Label("Invitation Code"), 0, 5);


        testButton.setOnAction(e -> {
            // Create an Alert of type INFORMATION
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            // Set the title and header text
            alert.setTitle("You found the Easter Egg!");
            alert.setHeaderText(null);

            // Load the GIF image
            Image gifImage = new Image("file:src/squidy.gif");
            ImageView gifImageView = new ImageView(gifImage);

            // Set properties for the image view to adjust size
            gifImageView.setFitWidth(200);  // Set the width
            gifImageView.setFitHeight(200); // Set the height
            gifImageView.setPreserveRatio(true); // Keep aspect ratio

            // Create a VBox to hold both the image and the text
            VBox vbox = new VBox();
            vbox.setSpacing(10); // Optional: Set spacing between image and text

            Label label = new Label("Project by: Luan, Meadow, Isabella, Smit and Gabriel <3");
            vbox.getChildren().addAll(gifImageView, label);

            // Add the VBox to the alert dialog
            alert.getDialogPane().setContent(vbox);

            // Show the alert dialog and wait for user response
            alert.showAndWait();
        });


        loginButton.setOnAction(e -> {
            String username = userNameField.getText();
            String password = passwordField.getText();
            try {
                if (dbUtil.login(username, password)) {
                    errorMessage.setText("Login successful");
                    User user = dbUtil.getUserByUsername(username);

                    // Proceed to the next scene or functionality
                    if (user.getRole().contains("Admin")) {
                        currentUser = "admin";
                        primaryStage.setScene(adminScene(primaryStage));
                    } else if (user.getRole().contains("Student")) {
                        //Set other scene depending on user role
                        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getMiddleName().isEmpty()) {
                            if (!user.getUsername().isEmpty()) {
                                primaryStage.setScene(finishSetupScene(primaryStage, user.getUsername()));
                            }
                        } else {
                            currentUser = "student";
                            primaryStage.setScene(studentScene(primaryStage));
                        }
                    } else if (user.getRole().contains("Instructor")) {
                        currentUser = "instructor";
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
                        dbUtil.resetUserDatabase();
                        dbUtil.resetInvitationDatabase();
                        dbUtil.resetHelpItemDatabase();
                        System.out.println("All databases reset successfully. Going back to Admin scene...");
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
        registerGrid.setHgap(10);
        registerGrid.setVgap(2);
        registerGrid.setAlignment(Pos.CENTER);

        //Invitation code
        TextField invitationCodeField = new TextField();
        invitationCodeField.setPromptText("Enter invitation code");
        invitationCodeField.getStyleClass().add("invitation-code-field");
        // Welcome label postioning and styling
        Label welcomeLabel = new Label("Create an Account");
        registerGrid.add(welcomeLabel, 0, 1); // Add welcome label to GridPane
        welcomeLabel.getStyleClass().add("register-label");
        // Username field positioning and styling
        TextField registerUserNameField = new TextField();
        registerUserNameField.getStyleClass().add("username-field");
        registerUserNameField.setPromptText("Enter a username");
        // Password field positioning and styling
        PasswordField registerPasswordField = new PasswordField();
        registerPasswordField.setPromptText("Enter a password");
        PasswordField registerConfirmPasswordField = new PasswordField();
        registerConfirmPasswordField.setPromptText("Confirm password");
        // Role ComboBox positioning and styling
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.setPromptText("Select a role");
        roleComboBox.getItems().addAll("Admin", "Student", "Instructor");
        roleComboBox.getStyleClass().add("role-combobox");
        // Create Account Button positioning and styling
        Button createAccountButton = new Button("Create Account");
        createAccountButton.getStyleClass().add("create-account-button");
        Button backToLoginButton = new Button("Back to login");
        backToLoginButton.getStyleClass().add("back-to-login-button");
        // Add all components to the Vbox container
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));
        container.getChildren().add(registerGrid);
        container.getStyleClass().add("border-container");


        // Create labels for password requirements, !!! I'm not sure how to put the Label initialization in the requirementsBox and update the text
        Label errorMessageLabel = new Label();
        errorMessageLabel.getStyleClass().add("error-message");

        Label requirementUpperCase = new Label("❌ Have one uppercase character");
        Label requirementLowerCase = new Label("❌ Have one lowercase character");
        Label requirementSpecialChar = new Label("❌ Have one special character");
        Label requirementLength = new Label("❌ Have 8 characters minimum");
        Label requirementMatches = new Label("❌ Passwords match");
        VBox requirementsBox = new VBox(4, requirementUpperCase, requirementLowerCase, requirementSpecialChar, requirementLength, requirementMatches);
        registerGrid.add(requirementsBox, 0, 5, 1, 1);
        // Add all components to the registerContainer VBox
        VBox registerContainer = new VBox(3, new Label("New Username: "),
                registerUserNameField, new Label("Password: "),
                registerPasswordField, new Label("Confirm Password: "),
                registerConfirmPasswordField);

        // Positioning of all components
        registerGrid.add(registerContainer, 0, 4, 1, 1);
        registerGrid.add(roleComboBox, 0, 6);
        registerGrid.add(createAccountButton, 0, 7);
        registerGrid.add(backToLoginButton, 0, 10);
        registerGrid.add(errorMessageLabel, 0, 11);
        registerGrid.add(invitationCodeField, 0, 3);
        registerGrid.add(new Label("Invitation Code:"), 0, 2);


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

        //
        createAccountButton.setOnAction(e -> {
            String username = registerUserNameField.getText();
            String password = registerPasswordField.getText();
            String invitationCode = invitationCodeField.getText();
            String confirmPassword = registerConfirmPasswordField.getText();
            String selectedRole = roleComboBox.getValue();
            // Check if the invitation code is valid
            try {
                System.out.println(dbUtil.isValidInvitationCode(invitationCode));
            } catch (SQLException s) {
                System.out.println("cooked");
            }

            // Check if the username, password, email, and invitation code are empty
            if (password.isEmpty() || username.isEmpty() || invitationCode.isEmpty()) {
                System.out.println("Username, password, invitation code cannot be empty!");
                errorMessageLabel.setText("Username, password, invite code cannot be empty!");
                // Check if the password meets the requirements
            } else if (!password.matches(".*[!@#$%^&*].*")) {
                System.out.println("Password must contain at least 1 Special Character");
            } else if (!password.matches(".*[a-z].*")) {
                System.out.println("Password must contain at least 1 Lower Case letter");
            } else if (!password.matches(".*[A-Z].*")) {
                System.out.println("Password must contain at least 1 Upper Case letter");
            } else if (!password.equals(confirmPassword)) {
                // Check if the password and confirm password match
                System.out.println("Password doesn't match");
            } else if (selectedRole == null || selectedRole.isEmpty()) {
                // Check if the selected role is empty
                System.out.println("Selected Role is empty");
            } else {
                try {
                    if (dbUtil.doesUserExist(username)) {
                        System.out.println("User already exists!");
                        errorMessageLabel.setText("User already exists!");
                    } else if (dbUtil.isValidInvitationCode(invitationCode)) {
                        dbUtil.register(username, password, selectedRole);
                        System.out.println("User registered successfully");
                        dbUtil.invalidateInvitationCode(invitationCode);
                        primaryStage.setScene(createLoginScene(primaryStage));
                    }
                } catch (SQLException ex) { // Catch any SQL exceptions
                    ex.printStackTrace(); // Print the stack trace
                }
            }
        });

        // Back to login button, returns to the login scene
        Scene registerScene = new Scene(container, WINDOW_HEIGHT, WINDOW_WIDTH);
        backToLoginButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));
        registerScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("register.css")).toExternalForm());
        createAccountButton.requestFocus(); // Set focus on the create account button, prevents highlight on text field
        return registerScene;
    }


    /**********
     * ADMIN SCENE
     * both instructor and admin can access this
     * @param primaryStage primaryStage
     * @return admin scene after successfully login as admin
     */
    private Scene adminScene(Stage primaryStage) {
        GridPane adminGrid = new GridPane();
        adminGrid.getStyleClass().add("root");
        adminGrid.setPadding(new Insets(30, 30, 30, 30));
        adminGrid.setHgap(15);
        adminGrid.setVgap(15);
        adminGrid.setAlignment(Pos.CENTER);

        // Header
        Label headerLabel = new Label("Admin Dashboard - General Group \uD83E\uDDD1\uD83C\uDFFC\u200D\uD83D\uDCBB");
        headerLabel.setAlignment((Pos.CENTER));
        headerLabel.getStyleClass().add("header-label");
        adminGrid.add(headerLabel, 0, 0, 2, 1);

        // User List
        ListView<String> userListView = new ListView<>();
        updateUserListView(userListView);
        userListView.setPrefHeight(200);
        adminGrid.add(userListView, 0, 1, 2, 1);

        // Button styles
        Button deleteUserButton = new Button("Delete User");
        Button addRoleButton = new Button("Add Role");
        Button removeRoleButton = new Button("Remove Role");
        Button inviteUserButton = new Button("Invite User");
        Button createHelpItemButton = new Button("Create Help Item");
        Button viewHelpItemsButton = new Button("View Help Items");
        Button viewSpecialAccessButton = new Button("View Special Access  Items");
        Button logoutButton = new Button("Log Out");
        Button backupButton = new Button("Backup Articles");
        Button backupSpecialAccessButton = new Button("Backup Special Access Articles");


        // Set preferred width for buttons
        deleteUserButton.setPrefWidth(150);
        addRoleButton.setPrefWidth(150);
        removeRoleButton.setPrefWidth(150);
        inviteUserButton.setPrefWidth(150);
        createHelpItemButton.setPrefWidth(150);
        viewHelpItemsButton.setPrefWidth(150);
        logoutButton.setPrefWidth(150);

        // Grouped button layout
        VBox buttonGroup1 = new VBox(10, deleteUserButton, addRoleButton, removeRoleButton, logoutButton, backupButton);
        VBox buttonGroup2 = new VBox(10, inviteUserButton, createHelpItemButton, viewHelpItemsButton,viewSpecialAccessButton , backupSpecialAccessButton);
        buttonGroup1.setAlignment(Pos.TOP_CENTER);
        buttonGroup2.setAlignment(Pos.TOP_CENTER);

        // Add to grid
        adminGrid.add(buttonGroup1, 0, 2);
        adminGrid.add(buttonGroup2, 1, 2);

        backupButton.setOnAction(e -> backupArticles());
        backupSpecialAccessButton.setOnAction((e -> backupSpecialAccessArticles()));
        // Delete user button, prompts user to confirm deleting user
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

        // Add role button, prompts user to enter a role to add to the selected user
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
        // Remove role button, prompts user to enter a role to remove from the selected user
        removeRoleButton.setOnAction(e -> {
            String selectedUser = userListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Remove Role");
                dialog.setHeaderText("Remove Role from User");
                // Create a ComboBox to select a role
                ComboBox<String> roleComboBox = new ComboBox<>();
                roleComboBox.getItems().addAll("Admin", "Student", "Instructor");
                roleComboBox.setPromptText("Select a role");
                // Set the content of the dialog pane to the ComboBox
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
        // Invite user button, prompts user to select a role to invite a new user
        inviteUserButton.setOnAction(e -> {
            ComboBox<String> roleComboBox = new ComboBox<>();
            roleComboBox.getItems().addAll("Admin", "Student", "Instructor");
            roleComboBox.setPromptText("Select a role");
            // Create a dialog to prompt the user to select a role
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Invite User");
            dialog.setHeaderText("Select a role");
            // Create a GridPane set padding
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            // Add Role label and postion
            grid.add(new Label("Role:"), 0, 0);
            grid.add(roleComboBox, 1, 0);

            dialog.getDialogPane().setContent(grid);
            ButtonType inviteButtonType = new ButtonType("Invite", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(inviteButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == inviteButtonType) {
                    return roleComboBox.getValue();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(role -> {
                if (role == null || role.isEmpty()) {
                    System.out.println("Role cannot be empty!");
                } else {
                    try {
                        String invitationCode = dbUtil.generateInvitationCode();
                        dbUtil.inviteUser(invitationCode, role);
                        TextInputDialog codeDialog = new TextInputDialog(invitationCode);
                        codeDialog.setTitle("Invitation Code");
                        codeDialog.setHeaderText("Copy the invitation code below:");
                        codeDialog.setContentText("Invitation Code:");
                        codeDialog.showAndWait();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });

        // Create help item button, prompts user to create a help item
        createHelpItemButton.setOnAction(e -> {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Create Help Item");
            dialog.setHeaderText("Create a new help item");

            // Set the button types
            ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

            TextField titleField = new TextField();
            titleField.setPromptText("Enter title");
            TextField descriptionField = new TextField();
            descriptionField.setPromptText("Enter description");
            TextField authorField = new TextField();
            authorField.setPromptText("Enter authors seperated by commas");
            TextField keywordsField = new TextField();
            keywordsField.setPromptText("Enter keywords seperated by commas");
            TextField referencesField = new TextField();
            referencesField.setPromptText("Enter references seperated by commas");
            TextField levelField = new TextField();
            levelField.setPromptText("Enter level: Beginner, Intermediate, Advanced, Expert");
            TextField categoryField = new TextField();
            categoryField.setPromptText("Enter category: Math, Science, History, etc.");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Description:"), 0, 1);
            grid.add(descriptionField, 1, 1);
            grid.add(new Label("Authors:"), 0, 2);
            grid.add(authorField, 1, 2);
            grid.add(new Label("Keywords:"), 0, 3);
            grid.add(keywordsField, 1, 3);
            grid.add(new Label("References:"), 0, 4);
            grid.add(referencesField, 1, 4);
            grid.add(new Label("Level:"), 0, 5);
            grid.add(levelField, 1, 5);
            grid.add(new Label("Category:"), 0, 6);
            grid.add(categoryField, 1, 6);



            // Enable/Disable the create button depending on whether a title was entered.
            Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
            createButton.setDisable(true);

            titleField.textProperty().addListener((observable, oldValue, newValue) -> {
                createButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            Platform.runLater(titleField::requestFocus);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButtonType) {
                    return new Pair<>(titleField.getText(), descriptionField.getText());
                }
                return null;
            });

            String shortDescription = descriptionField.getText().substring(0, Math.min(descriptionField.getText().length(), 50));

            dialog.showAndWait().ifPresent(title -> {
                try {
                    dbUtil.addHelpItem(
                            titleField.getText(),
                            descriptionField.getText(),
                            shortDescription,
                            authorField.getText(),
                            keywordsField.getText(),
                            referencesField.getText(),
                            levelField.getText(),
                            categoryField.getText()

                    );
                } catch (Exception ex) {
                    System.out.println("Error creating help item");
                }
            });
        });

        // View help items button, prompts user to view all help items
        viewHelpItemsButton.setOnAction(e -> {
            // Go to the help items scene
            System.out.println("Viewing help items...");
            primaryStage.setScene(helpItemsScene(primaryStage));
        });
        // Logout button, returns to the login scene
        logoutButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));
        // Set scene and stylesheet
        Scene adminScene = new Scene(adminGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
        adminScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("adminScene.css")).toExternalForm());
        return adminScene;
    }


    // Update the user list view
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
        currentUser = "student";
        GridPane studentSceneGrid = new GridPane();
        studentSceneGrid.setPadding(new Insets(20, 20, 20, 20));
        studentSceneGrid.setHgap(H_GAP);
        studentSceneGrid.setVgap(V_GAP);
        // Create all the components for the student scene
        Button backToLoginButton = new Button("Back to login");
        studentSceneGrid.add(new Label("Student Scene"), 0, 0);
        studentSceneGrid.add(backToLoginButton, 0, 1);

        // Back to login button, returns to the login scene
        backToLoginButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));
        // Create view help items button
        Button viewHelpItemsButton = new Button("View Help Items");
        studentSceneGrid.add(viewHelpItemsButton, 1, 1);
        // View help items button, prompts user to view all help items
        viewHelpItemsButton.setOnAction(e -> {
            System.out.println("Viewing help items...");
            primaryStage.setScene(helpItemsScene(primaryStage));
        });
        // Create a send help message button
        Button sendHelpMessageButton = new Button("Send Help Message");
        studentSceneGrid.add(sendHelpMessageButton, 0, 2);
        // Send help message button, prompts user to send a help message, opens a new window with a text area, and a drop down to select generic help or specific help
        sendHelpMessageButton.setOnAction(e -> {
            Stage helpMessageStage = new Stage();
            helpMessageStage.setTitle("Send Help Message");
            GridPane helpMessageGrid = new GridPane();
            helpMessageGrid.setPadding(new Insets(20, 20, 20, 20));
            helpMessageGrid.setHgap(H_GAP);
            helpMessageGrid.setVgap(V_GAP);
            // Create a text area for the help message
            TextArea helpMessageTextArea = new TextArea();
            helpMessageTextArea.setPromptText("Enter your help message here");
            helpMessageTextArea.setPrefRowCount(10);
            helpMessageTextArea.setPrefColumnCount(50);
            helpMessageGrid.add(helpMessageTextArea, 0, 0);
            // Create a ComboBox to select generic help or specific help
            ComboBox<String> helpTypeComboBox = new ComboBox<>();
            helpTypeComboBox.getItems().addAll("Generic Help", "Specific Help");
            helpTypeComboBox.setPromptText("Select help type");
            helpMessageGrid.add(helpTypeComboBox, 0, 1);
            // Create a send button to send the help message
            Button sendButton = new Button("Send");
            helpMessageGrid.add(sendButton, 0, 2);
            // Send button, sends the help message
            sendButton.setOnAction(event -> {
                String helpMessage = helpMessageTextArea.getText();
                String helpType = helpTypeComboBox.getValue();
                if (helpMessage.isEmpty() || helpType == null) {
                    System.out.println("Help message or help type cannot be empty!");
                } else {
                    System.out.println("Help message sent successfully");
                    helpMessageStage.close();
                }
            });
            // Add a back button to the help message window
            Button backToStudentSceneButton = new Button("Back");
            helpMessageGrid.add(backToStudentSceneButton, 0, 3);
            // Back button, returns to the student scene
            backToStudentSceneButton.setOnAction(event -> helpMessageStage.close());
            // Set the scene and show the stage
            Scene helpMessageScene = new Scene(helpMessageGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
            helpMessageStage.setScene(helpMessageScene);
            helpMessageStage.show();
        });
        return new Scene(studentSceneGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
    }

    /**********
     * INSTRUCTOR SCENE
     *
     * @param primaryStage primaryStage
     * @return instructor scene after successfully login
     */
    private Scene instructorScene(Stage primaryStage) {
        currentUser = "instructor";
        GridPane instructorGrid = new GridPane();
        instructorGrid.getStyleClass().add("root");
        instructorGrid.setPadding(new Insets(30, 30, 30, 30));
        instructorGrid.setHgap(15);
        instructorGrid.setVgap(15);
        instructorGrid.setAlignment(Pos.CENTER);

        // Header Label
        Label headerLabel = new Label("Instructor Dashboard - General Group 🧑‍🏫");
        headerLabel.setAlignment(Pos.CENTER);
        instructorGrid.add(headerLabel, 0, 0, 2, 1);

        // User List View
        ListView<String> userListView = new ListView<>();
        updateUserListView(userListView);
        userListView.setPrefHeight(200);
        instructorGrid.add(userListView, 0, 1, 2, 1);

        // Buttons
        Button createHelpItemButton = new Button("Create Help Item");
        Button viewHelpItemsButton = new Button("View Help Items");
        Button viewSpecialAccessButton = new Button("View Special Access Items");
        Button createSpecialAccessButton = new Button("Create Special Access Item");
        Button logoutButton = new Button("Log Out");
        Button backupButton = new Button("Backup Articles");
        backupButton.setOnAction(e -> backupArticles());

        createHelpItemButton.setPrefWidth(150);
        viewHelpItemsButton.setPrefWidth(150);
        logoutButton.setPrefWidth(150);

        VBox buttonGroup = new VBox(10, createHelpItemButton, viewHelpItemsButton, viewSpecialAccessButton, createSpecialAccessButton ,logoutButton, backupButton);
        instructorGrid.add(buttonGroup, 0, 2, 2, 1); // Corrected to span only 2 columns

        // Logout button action
        logoutButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));
        viewHelpItemsButton.setOnAction(e -> {
            // Go to the help items scene
            System.out.println("Viewing help items...");
            primaryStage.setScene(helpItemsScene(primaryStage));
        });

        // Create help item button, prompts user to create a help item
        createHelpItemButton.setOnAction(e -> {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Create Help Item");
            dialog.setHeaderText("Create a new help item");

            // Set the button types
            ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

            TextField titleField = new TextField();
            titleField.setPromptText("Enter title");
            TextField descriptionField = new TextField();
            descriptionField.setPromptText("Enter description");
            TextField authorField = new TextField();
            authorField.setPromptText("Enter authors seperated by commas");
            TextField keywordsField = new TextField();
            keywordsField.setPromptText("Enter keywords seperated by commas");
            TextField referencesField = new TextField();
            referencesField.setPromptText("Enter references seperated by commas");
            TextField levelField = new TextField();
            levelField.setPromptText("Enter level: Beginner, Intermediate, Advanced, Expert");
            TextField categoryField = new TextField();
            categoryField.setPromptText("Enter category: Math, Science, History, etc.");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Description:"), 0, 1);
            grid.add(descriptionField, 1, 1);
            grid.add(new Label("Authors:"), 0, 2);
            grid.add(authorField, 1, 2);
            grid.add(new Label("Keywords:"), 0, 3);
            grid.add(keywordsField, 1, 3);
            grid.add(new Label("References:"), 0, 4);
            grid.add(referencesField, 1, 4);
            grid.add(new Label("Level:"), 0, 5);
            grid.add(levelField, 1, 5);
            grid.add(new Label("Category:"), 0, 6);


            // Enable/Disable the create button depending on whether a title was entered.
            Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
            createButton.setDisable(true);

            titleField.textProperty().addListener((observable, oldValue, newValue) -> {
                createButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            Platform.runLater(titleField::requestFocus);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButtonType) {
                    return new Pair<>(titleField.getText(), descriptionField.getText());
                }
                return null;
            });

            String shortDescription = descriptionField.getText().substring(0, Math.min(descriptionField.getText().length(), 50));

            dialog.showAndWait().ifPresent(title -> {
                try {
                    dbUtil.addHelpItem(
                            titleField.getText(),
                            descriptionField.getText(),
                            shortDescription,
                            authorField.getText(),
                            keywordsField.getText(),
                            referencesField.getText(),
                            levelField.getText(),
                            categoryField.getText()
                    );
                } catch (Exception ex) {
                    System.out.println("Error creating help item");
                }
            });
        });

        Scene instructorScene = new Scene(instructorGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
        instructorScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("instructorScene.css")).toExternalForm());
        return instructorScene;
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
        // Create all the components for the finish setup scene
        TextField emailField = new TextField();
        TextField firstNameField = new TextField();
        TextField middleNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField preferredFirstNameField = new TextField();
        Button finishSetupButton = new Button("Finish Setup");
        // Positioning of all components
        finishSetupGrid.add(new Label("Finish setting up your account"), 0, 0, 4, 1);
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

        // Finish setup button, updates user details in the database
        finishSetupButton.setOnAction(e -> {
            String email = emailField.getText();
            String firstName = firstNameField.getText();
            String middleName = middleNameField.getText();
            String lastName = lastNameField.getText();
            String preferredFirstName = preferredFirstNameField.getText();
            // Check if email, first name, and last name are empty
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
        // Return the scene
        Scene finishSetupScene = new Scene(finishSetupGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
        finishSetupScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("finishSetup.css")).toExternalForm());
        return finishSetupScene;
    }

    /**
     * Creates the scene for viewing help items.
     * Users can view all help items in a list and click on an item to see more details.
     *
     * @param primaryStage The main stage.
     * @return Scene for viewing help items.
     */

    private Scene helpItemsScene(Stage primaryStage) {
        // GridPane for layout
        GridPane helpItemsGrid = new GridPane();
        helpItemsGrid.setPadding(new Insets(20));
        helpItemsGrid.setHgap(15);
        helpItemsGrid.setVgap(10);
        helpItemsGrid.getStyleClass().add("root");
        // Title for the scene
        Label titleLabel = new Label("Help Items 📚");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 0 10 0;");

        // Search box for filtering help items
        TextField searchBox = new TextField();
        searchBox.setPromptText("Search help items...");

        TextField groupBox = new TextField();
        groupBox.setPromptText("Filter by assignment group");

        // Create a dropdown box right under the search box, to filter by Title, Author or abstract
        ComboBox<String> filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("Title", "Author", "Abstract");
        filterComboBox.setPromptText("Title");
        ComboBox<String> filterLevelBox = new ComboBox<>();
        filterLevelBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert", "All Levels");
        filterLevelBox.setPromptText("All Levels");

        // ListView for displaying help items
        ListView<String> helpItemsListView = new ListView<>();

        // Load help items into an observable list
        ObservableList<String> helpItemsList = FXCollections.observableArrayList();
        try {
            List<helpItem> helpItems = dbUtil.getAllHelpItems();
            for (helpItem item : helpItems) {
                helpItemsList.add(item.getTitle());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Wrap the helpItemsList with a FilteredList for search functionality
        FilteredList<String> filteredHelpItems = new FilteredList<>(helpItemsList, s -> true);
        helpItemsListView.setItems(filteredHelpItems);

        // Update filter whenever the search box text changes
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredHelpItems.setPredicate(title -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (filterComboBox.getValue() == null || filterComboBox.getValue().equals("Title")) {
                    return title.toLowerCase().contains(lowerCaseFilter); // Filter based on title
                } else if (filterComboBox.getValue().equals("Author")) {
                    return dbUtil.getHelpItem(title).getAuthors().toLowerCase().contains(lowerCaseFilter); // Filter based on author
                } else if (filterComboBox.getValue().equals("Abstract")) {
                    return dbUtil.getHelpItem(title).getShortDescription().toLowerCase().contains(lowerCaseFilter); // Filter based on abstract
                } else if (!groupBox.getText().isEmpty()){
                    return dbUtil.getHelpItem(title).getGroup().toLowerCase().contains(groupBox.getText().toLowerCase());
                }
                return false;
            });
        });

        Button backToDashboard = new Button("Back to Dashboard");
        Button backToLoginButton = new Button("Back to Login");
        backToDashboard.setMinWidth(150);
        backToLoginButton.setMinWidth(150);

        // Layout for buttons and ListView
        VBox buttonBox = new VBox(10, backToDashboard, backToLoginButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        VBox listViewBox = new VBox(10, titleLabel,
                searchBox,
                groupBox,
                filterComboBox,
                helpItemsListView,
                buttonBox);
        listViewBox.setPadding(new Insets(10));
        listViewBox.setStyle("-fx-border-color: #ddd; -fx-border-width: 1px; -fx-border-radius: 5px;");
        helpItemsGrid.add(listViewBox, 0, 0);

        // Details section
        VBox itemDetailsBox = new VBox(10);
        itemDetailsBox.setPrefSize(500, 500);
        itemDetailsBox.setPadding(new Insets(10));
        itemDetailsBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Label itemIDLabel = new Label("ID:");
        Label itemID = new Label(); // Display the ID here, non-editable
        TextField titleField = new TextField();
        TextField levelField = new TextField();
        TextArea descriptionField = new TextArea();
        TextField authorsField = new TextField();
        TextField keywordsField = new TextField();
        TextField referencesField = new TextField();
        TextField groupField = new TextField();
        Button saveButton = new Button("Save Changes");
        Label confirmationMessage = new Label();

        if (!currentUser.equals("admin") && !currentUser.equals("instructor")) {
            titleField.setEditable(false);
            levelField.setEditable(false);
            descriptionField.setEditable(false);
            authorsField.setEditable(false);
            keywordsField.setEditable(false);
            referencesField.setEditable(false);
            saveButton.setDisable(true);
            saveButton.setStyle("-fx-background-color: grey;");
        }


        itemDetailsBox.getChildren().addAll(
                itemIDLabel, itemID,
                new Label("Title:"), titleField,
                new Label("Description:"), descriptionField,
                new Label("Authors:"), authorsField,
                new Label("Keywords:"), keywordsField,
                new Label("References:"), referencesField,
                new Label("Level:"), levelField,
                new Label("Group:"), groupField,
                saveButton, confirmationMessage
        );
        helpItemsGrid.add(itemDetailsBox, 1, 0);

        // ListView selection listener for displaying details
        helpItemsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                helpItem selectedHelpItem = dbUtil.getHelpItem(newValue);
                itemID.setText(String.valueOf(selectedHelpItem.getId())); // Display ID
                titleField.setText(selectedHelpItem.getTitle());
                descriptionField.setText(selectedHelpItem.getDescription());
                authorsField.setText(selectedHelpItem.getAuthors());
                keywordsField.setText(selectedHelpItem.getKeywords());
                referencesField.setText(selectedHelpItem.getReferences());
                levelField.setText(selectedHelpItem.getLevel());
                groupField.setText(selectedHelpItem.getGroup());
            }
        });

        saveButton.setOnAction(e -> {
            String selectedTitle = helpItemsListView.getSelectionModel().getSelectedItem();
            if (selectedTitle != null) {
                confirmationMessage.setText("Changes saved successfully!");
                String shortDescription = descriptionField.getText().substring(0, Math.min(descriptionField.getText().length(), 50));
                helpItem updatedItem = new helpItem(
                        Integer.parseInt(itemID.getText()), // Pass the ID for update
                        titleField.getText(),
                        descriptionField.getText(),
                        shortDescription,
                        authorsField.getText(),
                        keywordsField.getText(),
                        referencesField.getText(),
                        levelField.getText(),
                        groupField.getText()

                );
                try {
                    dbUtil.updateHelpItem(Integer.parseInt(itemID.getText()), updatedItem); // Update by ID
                    helpItemsListView.getItems().set(
                            helpItemsListView.getSelectionModel().getSelectedIndex(),
                            updatedItem.getTitle()
                    );
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        backToLoginButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));
        backToDashboard.setOnAction(e -> {
            if (currentUser.equals("admin")) {
                primaryStage.setScene(adminScene(primaryStage));
            } else if (currentUser.equals("instructor")) {
                primaryStage.setScene(instructorScene(primaryStage));
            } else {
                primaryStage.setScene(studentScene(primaryStage));
            }
        });

        Scene helpItemsScene = new Scene(helpItemsGrid, WINDOW_HEIGHT, WINDOW_WIDTH);
        helpItemsScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("helpItems.css")).toExternalForm());
        return helpItemsScene;
    }


    /**********
     * private void backupArticles()
     *
     *The program shall provide commands to back up the set of articles to a secondary storage file
     *with an operator-specified file name and to load a backed-up set of articles from an operator-specified file.
     *The restore operation first resets the application's set of articles to empty before loading the backed-up set.
     */
    private void backupArticles() {
        TextInputDialog dialog = new TextInputDialog("backup.csv");
        dialog.setTitle("Backup Articles");
        dialog.setHeaderText("Enter the file name for the backup:");
        dialog.setContentText("File Name:");

        dialog.showAndWait().ifPresent(fileName -> {
            try {
                dbUtil.backupHelpItemsToFile(fileName.trim());
                showAlert("Success", "Articles backed up to " + fileName);
            } catch (Exception e) {
                showAlert("Error", "Failed to backup articles: " + e.getMessage());
            }
        });
    }

    private void backupSpecialAccessArticles() {
        TextInputDialog dialog = new TextInputDialog("backup.csv");
        dialog.setTitle("Backup Special Access Articles");
        dialog.setHeaderText("Enter the file name for the backup:");
        dialog.setContentText("File Name:");

        dialog.showAndWait().ifPresent(fileName -> {
            try {
                dbUtil.backupSpecialAccessItemsToFile(fileName.trim());
                showAlert("Success", "Articles backed up to " + fileName);
            } catch (Exception e) {
                showAlert("Error", "Failed to backup articles: " + e.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
