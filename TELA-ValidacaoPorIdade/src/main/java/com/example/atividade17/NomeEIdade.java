package com.example.atividade17;

public class NomeEIdade {

    private int idade;

    private String nome;

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String validacao() {

        if (nome.length()  > 3 && idade >= 18) {

            return "Acesso permitido!";
        }

        return "Acesso negado!";

    }
}
