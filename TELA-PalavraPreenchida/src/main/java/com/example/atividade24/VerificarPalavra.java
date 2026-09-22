package com.example.atividade24;

public class VerificarPalavra {

    private String palavra;

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String PalavraPreenchida() {
        if (palavra != null && !palavra.isEmpty()) {

            return "A palavra não está vazia!";
        }
        return "A palavra está vazia!";
    }
}