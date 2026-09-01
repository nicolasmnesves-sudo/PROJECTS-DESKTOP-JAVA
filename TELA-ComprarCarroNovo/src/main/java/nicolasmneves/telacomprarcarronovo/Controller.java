package nicolasmneves.telacomprarcarronovo;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private Label lblVerificar;

    @FXML
    private CheckBox chxQuerCarro;

    @FXML
    private CheckBox chxTemDinheiro;

    @FXML
    protected void onButtonClick() {
        boolean temdinheiro = chxTemDinheiro.isSelected();
        boolean querCarro = chxQuerCarro.isSelected();

        if (temdinheiro && querCarro){
            lblVerificar.setText("Você pode comprar um carro");
        } else {
            lblVerificar.setText("Você não pode comprar um carro");
        }

    }
}
