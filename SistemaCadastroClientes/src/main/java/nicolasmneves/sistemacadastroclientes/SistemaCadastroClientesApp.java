package nicolasmneves.sistemacadastroclientes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SistemaCadastroClientesApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SistemaCadastroClientesApp.class.getResource("Clientes.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 600);
        stage.setTitle("Cadastro de Clientes");
        stage.setScene(scene);
        stage.show();
    }
}
