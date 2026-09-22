package silosgroup.projetosystem_silo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaPrincipalController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button btnReportarDivergenciaButton;

    @FXML
    private void mudarTela(ActionEvent event) throws IOException {

        Parent novaTela = FXMLLoader.load(getClass().getResource("TelaReportarDivergencia.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(novaTela);
        stage.setScene(scene);
        stage.show();
    }
}
