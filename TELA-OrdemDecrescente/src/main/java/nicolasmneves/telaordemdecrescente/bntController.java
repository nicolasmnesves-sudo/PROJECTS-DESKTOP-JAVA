package nicolasmneves.telaordemdecrescente;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            int numero1 = Integer.parseInt(txtNum1.getText());
            int numero2 = Integer.parseInt(txtNum2.getText());
            int numero3 = Integer.parseInt(txtNum3.getText());

            // Exemplo com os números
            List<Integer> numeros = Arrays.asList(numero1, numero2, numero3);

            // Ordena a lista em ordem decrescente
            numeros.sort(Collections.reverseOrder());

            // Imprime o resultado
            lblResult.setText(numeros.toString());

        }catch (NumberFormatException e){
            lblResult.setText("Valor invalido!");
        }





    }
}
