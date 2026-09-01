package nicolasmneves.telaimparoupar;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private Label lblResultado;

    @FXML
    private TextField lblNumero;

    @FXML
    protected void onHelloButtonClick() {
        try {
            int numero = Integer.parseInt(lblNumero.getText());

            if (numero % 2 == 0) {
                lblResultado.setText("PAR");
            } else {
                lblResultado.setText("ÍMPAR");
            }

        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("NUMERO INVALIDO");
            alerta.showAndWait();
        }
    }
}