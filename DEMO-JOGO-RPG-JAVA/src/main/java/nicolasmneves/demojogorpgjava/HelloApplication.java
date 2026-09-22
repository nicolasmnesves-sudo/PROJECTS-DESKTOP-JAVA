package nicolasmneves.demojogorpgjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 450);
        stage.setTitle("GAME");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}