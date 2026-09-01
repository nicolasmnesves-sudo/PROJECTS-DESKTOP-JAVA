package com.example.atividade25;

public class VerificarCancelamento {

    boolean cancelar;

    public boolean isCancelar() {
        return cancelar;
    }

    public void setCancelar(boolean cancelar) {
        this.cancelar = cancelar;
    }

    public String ConfirmacaoCancelamento() {

        if (cancelar) {

            return "Cancelamento bem sucedido";

        }
        return "Por favor, confirme o cancelamento da operação";
    }
}
