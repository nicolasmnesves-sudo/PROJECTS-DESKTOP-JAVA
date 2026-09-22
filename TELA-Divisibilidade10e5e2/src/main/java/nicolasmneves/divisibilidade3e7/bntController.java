package nicolasmneves.divisibilidade3e7;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class bntController {
    @FXML
    private Label lblResult;

    @FXML
    private TextField txtNumero;

    @FXML
    protected void onButtonClick() {

try {
    int numero = Integer.parseInt(txtNumero.getText());
    if (numero % 10 == 0 && numero % 5 == 0 && numero % 2 == 0) {
        lblResult.setText("o numero " + numero + " é divisível por 10, 5 e 2.");
    } else {
        lblResult.setText("o numero " + numero + " não é divisível por 10, 5 e 2.");
    }
}catch (NumberFormatException e){
    lblResult.setText("Digite um valor valido");
}



    }
}
