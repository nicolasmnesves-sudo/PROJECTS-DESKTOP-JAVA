package com.example.atividade17;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TelaPrincipalController {

    NomeEIdade verify = new NomeEIdade();

    int idade;
    String nome;

    @FXML
    private Label lblResultado;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtIdade;

    public void btnValidarAcesso(ActionEvent actionEvent) {

        try {

            idade = Integer.parseInt(txtIdade.getText());
            nome = txtNome.getText();

            verify.setIdade(idade);
            verify.setNome(nome);

            lblResultado.setText(verify.validacao());

        }  catch (NumberFormatException e) {

            lblResultado.setText("Digite uma idade válida");

        }
    }
}
