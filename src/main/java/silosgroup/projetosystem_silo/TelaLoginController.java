package silosgroup.projetosystem_silo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaLoginController {

    @FXML
    private VBox loginBox;

    @FXML
    private VBox cadastroBox;

    @FXML
    private TextField loginUsuario;

    @FXML
    private PasswordField loginSenha;

    @FXML
    private TextField cadastroUsuario;

    @FXML
    private PasswordField cadastroSenha;

    @FXML
    private PasswordField cadastroConfirmarSenha;


    @FXML
    private void mostrarCadastro() {
        loginBox.setVisible(false);
        loginBox.setManaged(false);

        cadastroBox.setVisible(true);
        cadastroBox.setManaged(true);
    }


    @FXML
    private void voltarLogin() {
        cadastroBox.setVisible(false);
        cadastroBox.setManaged(false);

        loginBox.setVisible(true);
        loginBox.setManaged(true);
    }


    @FXML
    private void entrar() {

        String usuario = loginUsuario.getText();
        String senha = loginSenha.getText();

        // Exemplo de login
        // Depois você pode substituir isso pela consulta ao banco de dados.
        if (usuario.equals("admin") && senha.equals("1234")) {

            abrirTelaPrincipal();

        } else {

            System.out.println("Usuário ou senha incorretos.");

        }
    }


    private void abrirTelaPrincipal() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("TelaPrincipal.fxml")
            );

            Parent telaPrincipal = loader.load();

            Scene scene = new Scene(telaPrincipal);

            Stage stage = (Stage) loginUsuario.getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Sistema Silo");

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }


    @FXML
    private void cadastrarUsuario() {

        String usuario = cadastroUsuario.getText();
        String senha = cadastroSenha.getText();
        String confirmarSenha = cadastroConfirmarSenha.getText();

        if (usuario.isBlank() || senha.isBlank()) {

            System.out.println("Preencha todos os campos.");

            return;
        }

        if (!senha.equals(confirmarSenha)) {

            System.out.println("As senhas não conferem.");

            return;
        }

        System.out.println("Usuário cadastrado: " + usuario);

        // Aqui você pode salvar o usuário no banco de dados.

        voltarLogin();
    }
}
