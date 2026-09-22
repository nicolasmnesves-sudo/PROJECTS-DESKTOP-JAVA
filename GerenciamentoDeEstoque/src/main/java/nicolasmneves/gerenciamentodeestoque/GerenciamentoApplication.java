package nicolasmneves.gerenciamentodeestoque;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GerenciamentoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GerenciamentoApplication.class.getResource("EstoqueView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920.0, 650.0);
        stage.setTitle("");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
