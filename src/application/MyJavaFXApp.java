package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.*;

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
	public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
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

        //Add login components to Login grid
        //GridPane parameters (node, column index, row index, column span, row span)
        loginGrid.add(new Label("Username"), 0 ,0);
        loginGrid.add(userNameField, 1, 0);
        loginGrid.add(new Label("Password"), 0, 1);
        loginGrid.add(passwordField, 1, 1);
        loginGrid.add(loginButton, 1, 2);
        loginGrid.add(registerButton, 1, 3);

        Scene loginScene = new Scene(loginGrid, 300, 200);

        //Button btn = new Button("Press");
        //btn.setOnAction(e -> System.out.println("Button Pressed"));
        //StackPane root = new StackPane();
        //root.getChildren().add(btn);
        // Scene scene = new Scene(root, 300, 250);


        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    
}