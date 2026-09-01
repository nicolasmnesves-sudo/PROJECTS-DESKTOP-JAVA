package com.controleestoque.model;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Produto {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty codigo = new SimpleStringProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final IntegerProperty idCategoria = new SimpleIntegerProperty();
    private final StringProperty nomeCategoria = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> precoCompra = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<BigDecimal> precoVenda = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final IntegerProperty estoque = new SimpleIntegerProperty();
    private final IntegerProperty estoqueMinimo = new SimpleIntegerProperty();
    private final BooleanProperty ativo = new SimpleBooleanProperty(true);
    private final ObjectProperty<LocalDateTime> dataCadastro = new SimpleObjectProperty<>();

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getCodigo() { return codigo.get(); }
    public void setCodigo(String codigo) { this.codigo.set(codigo); }
    public StringProperty codigoProperty() { return codigo; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }
    public StringProperty nomeProperty() { return nome; }

    public int getIdCategoria() { return idCategoria.get(); }
    public void setIdCategoria(int idCategoria) { this.idCategoria.set(idCategoria); }
    public IntegerProperty idCategoriaProperty() { return idCategoria; }

    public String getNomeCategoria() { return nomeCategoria.get(); }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria.set(nomeCategoria); }
    public StringProperty nomeCategoriaProperty() { return nomeCategoria; }

    public BigDecimal getPrecoCompra() { return precoCompra.get(); }
    public void setPrecoCompra(BigDecimal precoCompra) { this.precoCompra.set(precoCompra); }
    public ObjectProperty<BigDecimal> precoCompraProperty() { return precoCompra; }

    public BigDecimal getPrecoVenda() { return precoVenda.get(); }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda.set(precoVenda); }
    public ObjectProperty<BigDecimal> precoVendaProperty() { return precoVenda; }

    public int getEstoque() { return estoque.get(); }
    public void setEstoque(int estoque) { this.estoque.set(estoque); }
    public IntegerProperty estoqueProperty() { return estoque; }

    public int getEstoqueMinimo() { return estoqueMinimo.get(); }
    public void setEstoqueMinimo(int estoqueMinimo) { this.estoqueMinimo.set(estoqueMinimo); }
    public IntegerProperty estoqueMinimoProperty() { return estoqueMinimo; }

    public boolean isAtivo() { return ativo.get(); }
    public void setAtivo(boolean ativo) { this.ativo.set(ativo); }
    public BooleanProperty ativoProperty() { return ativo; }

    public LocalDateTime getDataCadastro() { return dataCadastro.get(); }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro.set(dataCadastro); }
    public ObjectProperty<LocalDateTime> dataCadastroProperty() { return dataCadastro; }

    /** Retorna true quando o estoque atual é menor ou igual ao estoque mínimo. */
    public boolean isEstoqueBaixo() {
        return getEstoque() <= getEstoqueMinimo();
    }
}
