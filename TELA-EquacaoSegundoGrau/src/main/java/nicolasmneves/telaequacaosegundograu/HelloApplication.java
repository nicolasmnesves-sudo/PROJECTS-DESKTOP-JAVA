package nicolasmneves.telaequacaosegundograu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TELA.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 310, 460);
        stage.setTitle("Equacao-Segundo-Grau");
        stage.setScene(scene);
        stage.show();
    }
}
