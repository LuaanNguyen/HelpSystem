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
        Button btn = new Button("Don't Press this");
        btn.setOnAction(e -> System.out.println("You son of a gun"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("2nd Commit");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
}