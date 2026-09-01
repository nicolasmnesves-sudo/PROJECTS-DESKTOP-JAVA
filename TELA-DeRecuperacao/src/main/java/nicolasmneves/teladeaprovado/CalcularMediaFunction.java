package nicolasmneves.teladeaprovado;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CalcularMediaFunction {
    @FXML
    private Label lblResultado;

    @FXML
    private Label lblResultadoAR;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtNota1;

    @FXML
    private TextField txtNota2;

    @FXML
    private TextField txtNota3;

    @FXML
    protected void bntCalcularMedia() {

        String nome = txtNome.getText();

        try {

            double nota1 = Double.parseDouble(txtNota1.getText());
            double nota2 = Double.parseDouble(txtNota2.getText());
            double nota3 = Double.parseDouble(txtNota3.getText());

            double media = (nota1 + nota2 + nota3) / 3;

            lblResultado.setText(String.format("%s, sua média é: %.1f", nome, media));


            if (media >= 7) {
                lblResultadoAR.setText("APROVADO");
            } else if(media <= 5) {
                lblResultadoAR.setText("REPROVADO");
            } else {
                lblResultadoAR.setText("RECUPERAÇÂO");
            }

        } catch (NumberFormatException e){
            lblResultadoAR.setText("Digite um valor valido...");
        }




    }
}
