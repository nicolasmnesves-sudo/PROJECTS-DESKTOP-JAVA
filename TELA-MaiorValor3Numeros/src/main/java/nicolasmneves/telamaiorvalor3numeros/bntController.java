package nicolasmneves.telamaiorvalor3numeros;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class bntController {
    @FXML
    private Label lblResult;

    @FXML
    private TextField txtNum1;

    @FXML
    private TextField txtNum2;

    @FXML
    private TextField txtNum3;

    @FXML
    protected void onButtonClick() {
        try {
            int a = Integer.parseInt(txtNum1.getText());
            int b = Integer.parseInt(txtNum2.getText());
            int c = Integer.parseInt(txtNum3.getText());

            int maior = Math.max(Math.max(a, b), c);

            lblResult.setText("O maior número entre " + a + ", " + b + " e " + c + " é: " + maior);
        }catch (NumberFormatException e){
            lblResult.setText("Número invalido...");
        }
    }
}
