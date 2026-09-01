package com.example.atividade16;

public class VerificacaoNovaTemporada {

    private boolean assinante;
    private boolean assistiu;

    public boolean isAssinante() {
        return assinante;
    }

    public void setAssinante(boolean assinante) {
        this.assinante = assinante;
    }

    public boolean isAssistiu() {
        return assistiu;
    }

    public void setAssistiu(boolean assistiu) {
        this.assistiu = assistiu;
    }

    public String verificar(){

        if(this.assinante == true && this.assistiu == true){

            return "Ótimo, agora você pode assistir à nova temporada!";
        }

        return "Você não pode assistir a nova temporada!";
    }
}

