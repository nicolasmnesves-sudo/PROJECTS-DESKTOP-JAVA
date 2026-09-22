package nicolasmneves.divisibilidade3e7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TELA.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 260, 300);
        stage.setTitle("DIVISIBILIDADE");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
