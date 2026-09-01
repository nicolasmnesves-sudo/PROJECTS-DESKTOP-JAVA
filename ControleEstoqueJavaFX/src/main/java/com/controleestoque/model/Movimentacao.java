package com.controleestoque.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Movimentacao {

    public static final String ENTRADA = "ENTRADA";
    public static final String SAIDA = "SAIDA";

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty idProduto = new SimpleIntegerProperty();
    private final StringProperty nomeProduto = new SimpleStringProperty();
    private final StringProperty tipo = new SimpleStringProperty();
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> dataMovimentacao = new SimpleObjectProperty<>();
    private final StringProperty observacao = new SimpleStringProperty();

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public int getIdProduto() { return idProduto.get(); }
    public void setIdProduto(int idProduto) { this.idProduto.set(idProduto); }
    public IntegerProperty idProdutoProperty() { return idProduto; }

    public String getNomeProduto() { return nomeProduto.get(); }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto.set(nomeProduto); }
    public StringProperty nomeProdutoProperty() { return nomeProduto; }

    public String getTipo() { return tipo.get(); }
    public void setTipo(String tipo) { this.tipo.set(tipo); }
    public StringProperty tipoProperty() { return tipo; }

    public int getQuantidade() { return quantidade.get(); }
    public void setQuantidade(int quantidade) { this.quantidade.set(quantidade); }
    public IntegerProperty quantidadeProperty() { return quantidade; }

    public LocalDateTime getDataMovimentacao() { return dataMovimentacao.get(); }
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) { this.dataMovimentacao.set(dataMovimentacao); }
    public ObjectProperty<LocalDateTime> dataMovimentacaoProperty() { return dataMovimentacao; }

    public String getObservacao() { return observacao.get(); }
    public void setObservacao(String observacao) { this.observacao.set(observacao); }
    public StringProperty observacaoProperty() { return observacao; }
}
