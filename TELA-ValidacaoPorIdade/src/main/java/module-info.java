module com.example.atividade17 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.atividade17 to javafx.fxml;
    exports com.example.atividade17;
}