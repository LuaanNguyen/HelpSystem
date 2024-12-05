module CSE360_BinaryBabes {
    requires javafx.controls;
    requires com.h2database;
    requires org.bouncycastle.provider;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires junit;

    exports application;
    opens application to javafx.graphics, javafx.fxml, junit;
}