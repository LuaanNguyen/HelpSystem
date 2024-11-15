module CSE360_BinaryBabes {
    requires javafx.controls;
    requires com.h2database;
    requires org.bouncycastle.lts.prov;

    opens application to javafx.graphics, javafx.fxml;
}