module nicolasmneves.demojogorpgjava {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.demojogorpgjava to javafx.fxml;
    exports nicolasmneves.demojogorpgjava;
}