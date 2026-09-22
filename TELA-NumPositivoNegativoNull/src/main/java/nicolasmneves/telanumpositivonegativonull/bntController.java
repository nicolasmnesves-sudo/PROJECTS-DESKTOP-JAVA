package nicolasmneves.telanumpositivonegativonull;

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

        int num = Integer.parseInt(txtNumero.getText());
            if (num >= 0){
                lblResult.setText("POSITIVO");
            } else {
                lblResult.setText("NEGATIVO");
            }


        } catch (NumberFormatException e){
            lblResult.setText("NULO");
        }
    }
}
