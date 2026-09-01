module nicolasmneves.teladeaprovado {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.teladeaprovado to javafx.fxml;
    exports nicolasmneves.teladeaprovado;
}