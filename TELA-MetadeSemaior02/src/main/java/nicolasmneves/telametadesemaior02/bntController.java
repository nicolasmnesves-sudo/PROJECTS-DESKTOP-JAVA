package nicolasmneves.telametadesemaior02;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class bntController {
    @FXML
    private Label result;

    @FXML
    private TextField num;

    @FXML
    protected void ButtonClick1(ActionEvent actionEvent) {
        try {
            double num1 = Double.parseDouble(num.getText());

            if (num1 > 20) {
                num1 = num1 / 2;
                result.setText(String.valueOf(num1));
            } else {
                result.setText("O numero é menor que 20!");
            }

        } catch (NumberFormatException e) {
            result.setText("Digite um valor valido");
        }
    }
}
