module nicolasmneves.sistemacadastroclientes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens nicolasmneves.sistemacadastroclientes to javafx.fxml;
    exports nicolasmneves.sistemacadastroclientes;
}