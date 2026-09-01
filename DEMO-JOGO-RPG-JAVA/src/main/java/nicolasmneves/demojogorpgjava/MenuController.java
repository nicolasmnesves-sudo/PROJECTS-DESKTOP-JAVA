package nicolasmneves.demojogorpgjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    // Variáveis estáticas para guardar a seleção (igual ao seu segundo código)
    public static String caminhoImagem;
    public static Personagem personagemSelecionado;

    @FXML
    private Button bntMago;

    @FXML
    private Button bntGuerreiro;

    @FXML
    public void btnSelecionarMago(ActionEvent actionEvent) {
        caminhoImagem = "/Imagens/mago_batalha.png";
        personagemSelecionado = new Mago();

        trocarTela(actionEvent);
    }

    @FXML
    public void btnSelecionarGuerreiro(ActionEvent actionEvent) {
        caminhoImagem = "/Imagens/guerreiro_batalha.png";
        personagemSelecionado = new Guerreiro();

        trocarTela(actionEvent);
    }

    private void trocarTela(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/nicolasmneves/demojogorpgjava/batalha-view.fxml"));

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}