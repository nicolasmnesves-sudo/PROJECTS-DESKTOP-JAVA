package nicolasmneves.telaconfirmacaodecompra;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class Controller {
    @FXML
    private Label lblResult;

    @FXML
    private CheckBox chxConfirmar;

    @FXML
    protected void onButtonClick() {
        boolean confirmar = chxConfirmar.isSelected();

        if (confirmar) {
            lblResult.setText("Compra realizada com sucesso");
        } else {
            lblResult.setText("Falha, compra não realizada");
        }
    }
}

