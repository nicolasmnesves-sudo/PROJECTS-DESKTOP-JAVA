module nicolasmneves.telaverificarusuario {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.telaverificarusuario to javafx.fxml;
    exports nicolasmneves.telaverificarusuario;
}