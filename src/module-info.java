module CSE360_BinaryBabes {
    requires javafx.controls;
    requires java.sql;
    requires com.h2database;

    opens application to javafx.graphics, javafx.fxml;
}