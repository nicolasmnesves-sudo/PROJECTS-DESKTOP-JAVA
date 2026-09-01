module nicolasmneves.telanumeroefaixaesperada {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens nicolasmneves.telanumeroefaixaesperada to javafx.fxml;
    exports nicolasmneves.telanumeroefaixaesperada;
}