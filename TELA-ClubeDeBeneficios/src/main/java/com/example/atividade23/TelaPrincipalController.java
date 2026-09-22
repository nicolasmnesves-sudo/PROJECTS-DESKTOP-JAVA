package com.example.atividade23;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class TelaPrincipalController {

    ClubeBeneficios verificarinscricao =  new ClubeBeneficios();

    boolean membro;

    @FXML
    private Label lblResultado;

    @FXML
    private RadioButton rdbSim;

    public void btnVerificarInscricao(ActionEvent actionEvent) {

        membro = rdbSim.isSelected();

        verificarinscricao.setMembro(membro);

        lblResultado.setText(verificarinscricao.VerificarMembro());

    }
}
