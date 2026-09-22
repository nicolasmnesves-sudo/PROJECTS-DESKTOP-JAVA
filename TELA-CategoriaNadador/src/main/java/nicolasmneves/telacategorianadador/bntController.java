package nicolasmneves.telacategorianadador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class bntController {
    @FXML
    private Label lblresult;

    @FXML
    private TextField txtIdade;

    @FXML
    protected void onButtonClick() {
        try{
            int idade = Integer.parseInt(txtIdade.getText());

            if (idade >= 5 && idade <= 7){
                lblresult.setText("Infantil A");
            } else if (idade >= 8 && idade <= 10){
                lblresult.setText("Infantil B");
            } else if (idade >= 11 && idade <= 13){
                lblresult.setText("Juvenil A");
            } else if (idade >= 14 && idade <= 17){
                lblresult.setText("Juvenil B");
            } else if (idade >= 18 && idade <= 100){
                lblresult.setText("Sênior");
            } else {
                lblresult.setText("Digite uma idade valida");
            }
        }catch (NumberFormatException e){
            lblresult.setText("ERROR, digite um valor valido");
        }

    }
}
