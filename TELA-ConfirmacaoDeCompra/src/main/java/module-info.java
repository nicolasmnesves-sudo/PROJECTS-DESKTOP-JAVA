module nicolasmneves.telaconfirmacaodecompra {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.telaconfirmacaodecompra to javafx.fxml;
    exports nicolasmneves.telaconfirmacaodecompra;
}