package nicolasmneves.telapermissaodirigir;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Spinner<Integer> mySpinner;

    @FXML
    private Label myLabel;

    @FXML
    private CheckBox myCheckBox;

    int currentValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);

        valueFactory.setValue(18);

        mySpinner.setValueFactory(valueFactory);


        }

    @FXML
    public void verificarPermissao() {

        int idade = mySpinner.getValue();


        boolean temCarteira = myCheckBox.isSelected();


        if (idade >= 18 && temCarteira) {
            myLabel.setText("Você tem permissão para dirigir!");
        } else if (idade >= 18 && !temCarteira) {
            myLabel.setText("Você tem idade, mas não tem carteira.");
        } else {
            myLabel.setText("Você não tem permissão para dirigir.");
        }
    }
}