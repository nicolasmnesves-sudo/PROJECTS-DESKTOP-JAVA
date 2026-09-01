package com.example.atividade25;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class TelaPrincipalController {

    VerificarCancelamento verificarCancelamento = new VerificarCancelamento();

    boolean cancelar;

    @FXML
    private Label lblResultado;

    @FXML
    private RadioButton rdbSimCancelar;

    public void btnVerificarCancelamento(ActionEvent actionEvent) {

        cancelar = rdbSimCancelar.isSelected();

        verificarCancelamento.setCancelar(cancelar);

        lblResultado.setText(verificarCancelamento.ConfirmacaoCancelamento());
    }
}
