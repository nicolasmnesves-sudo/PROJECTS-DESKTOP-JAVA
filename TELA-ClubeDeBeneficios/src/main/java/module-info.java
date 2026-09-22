module com.example.atividade23 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.atividade23 to javafx.fxml;
    exports com.example.atividade23;
}