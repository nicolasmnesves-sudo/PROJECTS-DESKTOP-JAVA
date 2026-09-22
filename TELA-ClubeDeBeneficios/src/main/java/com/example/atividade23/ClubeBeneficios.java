package com.example.atividade23;

public class ClubeBeneficios {

    private boolean membro;

    public boolean isMembro() {
        return membro;
    }

    public void setMembro(boolean membro) {
        this.membro = membro;
    }

    public String VerificarMembro() {

        if (!membro) {

            return "Por favor, atualize sua inscrição para continuar usufruindo dos benefícios do clube";

        }
        return "Você é um membro ativo!";
    }
}
