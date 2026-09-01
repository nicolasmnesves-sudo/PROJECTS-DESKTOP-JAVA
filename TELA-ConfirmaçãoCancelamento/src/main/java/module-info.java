module com.example.atividade25 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.atividade25 to javafx.fxml;
    exports com.example.atividade25;
}