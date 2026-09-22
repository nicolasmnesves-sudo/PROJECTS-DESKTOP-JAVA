module nicolasmneves.estacionamento {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.estacionamento to javafx.fxml;
    exports nicolasmneves.estacionamento;
}