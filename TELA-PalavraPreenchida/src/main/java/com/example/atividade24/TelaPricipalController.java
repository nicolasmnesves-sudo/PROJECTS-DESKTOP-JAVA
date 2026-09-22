package com.example.atividade24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TelaPricipalController {

    VerificarPalavra verificar = new VerificarPalavra();

    String palavra;

    @FXML
    private Label lblResultado;

    @FXML
    private TextField txtPalavra;

    public void btnVerificarPalavra(ActionEvent actionEvent) {

        palavra = txtPalavra.getText();

        verificar.setPalavra(palavra);

        lblResultado.setText(verificar.PalavraPreenchida());
    }
}
