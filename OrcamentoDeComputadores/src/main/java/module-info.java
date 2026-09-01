module nicolasmneves.orcamentodecomputadores {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.orcamentodecomputadores to javafx.fxml;
    exports nicolasmneves.orcamentodecomputadores;
}