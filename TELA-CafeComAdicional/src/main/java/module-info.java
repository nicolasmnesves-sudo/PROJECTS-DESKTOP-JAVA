module nicolasmneves.telacafecomadicional {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.telacafecomadicional to javafx.fxml;
    exports nicolasmneves.telacafecomadicional;
}