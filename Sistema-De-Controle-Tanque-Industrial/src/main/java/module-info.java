module nicolasmneves.sistemadecontroletanqueindustrial {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.sistemadecontroletanqueindustrial to javafx.fxml;
    exports nicolasmneves.sistemadecontroletanqueindustrial;
}