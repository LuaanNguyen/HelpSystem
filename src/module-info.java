module CSE360_BinaryBabes {
    requires javafx.controls;
    requires com.h2database;
    requires org.bouncycastle.provider;
    requires java.sql;

    opens application to javafx.graphics, javafx.fxml;
}