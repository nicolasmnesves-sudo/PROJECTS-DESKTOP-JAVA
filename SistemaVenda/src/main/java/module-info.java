module nicolasmneves.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.demo to javafx.fxml;
    exports nicolasmneves.demo;
}