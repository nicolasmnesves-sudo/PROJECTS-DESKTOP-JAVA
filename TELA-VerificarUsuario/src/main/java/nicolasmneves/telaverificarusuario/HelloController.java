package nicolasmneves.telaverificarusuario;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label lblResult;

    @FXML
    private TextField txtNome;

    @FXML
    protected void onHelloButtonClick() {
        if (txtNome.getText().equals("Wilson") || txtNome.getText().equals("Gloria")) {
            lblResult.setText("Olá, bem vindo(a) de volta!");
        } else {
            lblResult.setText("Usuário incorreto");
        }


    }
}
