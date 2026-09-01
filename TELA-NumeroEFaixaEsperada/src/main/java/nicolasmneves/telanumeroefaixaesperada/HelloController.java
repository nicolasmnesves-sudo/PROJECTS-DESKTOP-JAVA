package nicolasmneves.telanumeroefaixaesperada;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.text.TabExpander;

public class HelloController {
    @FXML
    private Label lblResult;

    @FXML
    private TextField txtNum;


    @FXML
    protected void onBntClick() {
        int num = Integer.parseInt(txtNum.getText());
        if (num > 10 || num < 0){
            lblResult.setText("Numero invalido");
        } else {
            lblResult.setText("Numero valido");
        }

    }
}
