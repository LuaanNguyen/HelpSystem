package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MyJavaFXApp extends Application {
	public static void main(String[] args) {
        launch(args);
    }
    @Override
    
    public void start(Stage primaryStage) {
    	System.out.println("Hello");
        Button btn = new Button("Click me!");
        btn.setOnAction(e -> System.out.println("Button clicked!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Ur mom stiny");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
}