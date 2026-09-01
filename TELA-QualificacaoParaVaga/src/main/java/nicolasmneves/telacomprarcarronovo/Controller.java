package nicolasmneves.telacomprarcarronovo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Spinner<Integer> mySpinner;

    @FXML
    private Label myLabel;

    @FXML
    private CheckBox myCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Define o intervalo de 1 a 100 anos, começando em 18
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 18);

        mySpinner.setValueFactory(valueFactory);
    }


    @FXML
    public void verificarPermissao() {

        int idade = mySpinner.getValue();

        boolean temDiploma = myCheckBox.isSelected();


        if (idade >= 21 && temDiploma) {
            myLabel.setText("PARABENS a vaga é sua");
        } else if (idade >= 21 && !temDiploma) {
            myLabel.setText("Você não passou");
        } else {
            myLabel.setText("Você não passou");
        }
    }
}