module nicolasmneves.controledeestoque {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.controledeestoque to javafx.fxml;
    exports nicolasmneves.controledeestoque;
}