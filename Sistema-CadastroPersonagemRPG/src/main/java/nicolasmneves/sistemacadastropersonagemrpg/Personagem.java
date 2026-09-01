package nicolasmneves.sistemacadastropersonagemrpg;

import java.io.Serializable;

public class Personagem implements Serializable {
    private String nome;
    private String classe;
    private String raca;
    private String origem;
    private int forca;
    private int defesa;
    private int agilidade;
    private int inteligencia;
    private String caminhoImagem;

    public Personagem() {
        this.caminhoImagem = "";
    }

//                      -=-=-=-=-=-=-=--=-=-=-=-=-=-=--=-=-=-=-=-=-=--=-=-=-=-=-=-=-

    public Personagem(String nome, String classe, String raca, String origem,
                      int forca, int defesa, int agilidade, int inteligencia, String caminhoImagem) {
        this.nome = nome;
        this.classe = classe;
        this.raca = raca;
        this.origem = origem;
        this.forca = forca;
        this.defesa = defesa;
        this.agilidade = agilidade;
        this.inteligencia = inteligencia;
        this.caminhoImagem = caminhoImagem != null ? caminhoImagem : "";
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public int getForca() { return forca; }
    public void setForca(int forca) { this.forca = forca; }

    public int getDefesa() { return defesa; }
    public void setDefesa(int defesa) { this.defesa = defesa; }

    public int getAgilidade() { return agilidade; }
    public void setAgilidade(int agilidade) { this.agilidade = agilidade; }

    public int getInteligencia() { return inteligencia; }
    public void setInteligencia(int inteligencia) { this.inteligencia = inteligencia; }

    public String getCaminhoImagem() { return caminhoImagem; }
    public void setCaminhoImagem(String caminhoImagem) { this.caminhoImagem = caminhoImagem; }

//                      -=-=-=-=-=-=-=--=-=-=-=-=-=-=--=-=-=-=-=-=-=--=-=-=-=-=-=-=-

    // Converte o objeto para linha CSV
    public String toCsv() {
        return String.join(",",
                escapeCsv(nome),
                escapeCsv(classe),
                escapeCsv(raca),
                escapeCsv(origem),
                String.valueOf(forca),
                String.valueOf(defesa),
                String.valueOf(agilidade),
                String.valueOf(inteligencia),
                escapeCsv(caminhoImagem)
        );
    }

    // Cria o objeto a partir de uma linha CSV
    public static Personagem fromCsv(String linha) {
        String[] partes = linha.split(",");
        if (partes.length < 8) return null;

        String nome = partes[0];
        String classe = partes[1];
        String raca = partes[2];
        String origem = partes[3];
        int forca = Integer.parseInt(partes[4]);
        int defesa = Integer.parseInt(partes[5]);
        int agilidade = Integer.parseInt(partes[6]);
        int inteligencia = Integer.parseInt(partes[7]);
        String imagem = partes.length > 8 ? partes[8] : "";

        return new Personagem(nome, classe, raca, origem, forca, defesa, agilidade, inteligencia, imagem);
    }

    private String escapeCsv(String dado) {
        return dado == null ? "" : dado.replace(",", "");
    }
}