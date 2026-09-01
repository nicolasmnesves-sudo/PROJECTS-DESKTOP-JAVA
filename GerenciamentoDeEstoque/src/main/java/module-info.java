module nicolasmneves.gerenciamentodeestoque {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.gerenciamentodeestoque to javafx.fxml;
    exports nicolasmneves.gerenciamentodeestoque;
}