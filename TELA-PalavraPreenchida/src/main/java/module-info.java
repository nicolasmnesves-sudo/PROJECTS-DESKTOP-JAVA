module com.example.atividade24 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.atividade24 to javafx.fxml;
    exports com.example.atividade24;
}