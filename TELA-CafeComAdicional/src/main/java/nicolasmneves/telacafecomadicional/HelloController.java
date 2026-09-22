package nicolasmneves.telacafecomadicional;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label lblResult;

    @FXML
    private CheckBox chxAddMilk;

    @FXML
    private CheckBox chxAddSugar;

    @FXML
    protected void onButtonClick() {
        boolean milk = chxAddMilk.isSelected();
        boolean sugar = chxAddSugar.isSelected();
    if (milk && sugar){
        lblResult.setText("café com açucar e leite");
    } else if (milk) {
        lblResult.setText("café com leite sem açucar");
    } else if (sugar) {
        lblResult.setText("café com açucar sem leite");
    } else {
        lblResult.setText("café sem adicionais");
    }
    }
}
