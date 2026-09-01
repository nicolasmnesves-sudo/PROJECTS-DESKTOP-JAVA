module com.example.atividade16 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.atividade16 to javafx.fxml;
    exports com.example.atividade16;
}