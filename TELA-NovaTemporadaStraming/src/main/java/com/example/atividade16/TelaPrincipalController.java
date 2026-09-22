package com.example.atividade16;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class TelaPrincipalController {

    VerificacaoNovaTemporada verificar = new VerificacaoNovaTemporada();

    boolean assinante;
    boolean jaAssistiu;

    @FXML
    private Label lblResultado;

    @FXML
    private RadioButton rdbSimAssinante;

    @FXML
    private RadioButton rdbNaoAssinante;

    @FXML
    private RadioButton rdbSimSerie;

    @FXML
    private RadioButton rdbNaoSerie;

    public void btnVerificarTemporada(ActionEvent actionEvent) {

        if (rdbSimAssinante.isSelected()) {
            assinante = true;
        }  else if (rdbNaoAssinante.isSelected()) {
            assinante = false;
        }

        if (rdbSimSerie.isSelected()) {
            jaAssistiu = true;
        } else if(rdbNaoSerie.isSelected()) {
            jaAssistiu = false;
        }

        verificar.setAssinante(assinante);
        verificar.setAssistiu(jaAssistiu);

        lblResultado.setText(verificar.verificar());
    }
}
