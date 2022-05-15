module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.frontend to javafx.fxml;
    exports com.frontend;
}