module nicolasmneves.boletimescolar {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.boletimescolar to javafx.fxml;
    exports nicolasmneves.boletimescolar;
}