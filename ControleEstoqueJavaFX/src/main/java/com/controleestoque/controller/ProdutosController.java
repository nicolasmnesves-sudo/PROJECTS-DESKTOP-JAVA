package com.controleestoque.controller;

import com.controleestoque.data.CategoriaDAO;
import com.controleestoque.data.ProdutoDAO;
import com.controleestoque.model.Categoria;
import com.controleestoque.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ProdutosController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;
    @FXML private ComboBox<Categoria> cboCategoria;
    @FXML private TextField txtPrecoCompra;
    @FXML private TextField txtPrecoVenda;
    @FXML private TextField txtEstoqueMinimo;
    @FXML private Label lblEstoqueAtual;
    @FXML private Button btnAtivarDesativar;
    @FXML private TextField txtPesquisa;
    @FXML private Label lblMensagem;

    @FXML private TableView<Produto> tabela;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, BigDecimal> colPrecoCompra;
    @FXML private TableColumn<Produto, BigDecimal> colPrecoVenda;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, Integer> colEstoqueMinimo;
    @FXML private TableColumn<Produto, Boolean> colAtivo;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ObservableList<Produto> dados = FXCollections.observableArrayList();
    private Produto produtoSelecionado;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nomeCategoria"));
        colPrecoCompra.setCellValueFactory(new PropertyValueFactory<>("precoCompra"));
        colPrecoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colEstoqueMinimo.setCellValueFactory(new PropertyValueFactory<>("estoqueMinimo"));
        colAtivo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().isAtivo()));
        colAtivo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean ativo, boolean empty) {
                super.updateItem(ativo, empty);
                setText(empty || ativo == null ? "" : (ativo ? "Sim" : "Não"));
            }
        });

        tabela.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Produto produto, boolean empty) {
                super.updateItem(produto, empty);
                if (empty || produto == null) {
                    setStyle("");
                } else if (produto.isEstoqueBaixo()) {
                    setStyle("-fx-background-color: #fdecea;");
                } else {
                    setStyle("");
                }
            }
        });

        tabela.setItems(dados);

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            produtoSelecionado = novo;
            preencherFormulario(novo);
        });

        carregarCategorias();
        carregarProdutos();
    }

    private void carregarCategorias() {
        try {
            ObservableList<Categoria> categorias = FXCollections.observableArrayList(categoriaDAO.listar());
            cboCategoria.setItems(categorias);
        } catch (SQLException e) {
            lblMensagem.setText("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void carregarProdutos() {
        try {
            dados.setAll(produtoDAO.listar());
        } catch (SQLException e) {
            lblMensagem.setText("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void preencherFormulario(Produto p) {
        if (p == null) {
            novo();
            return;
        }
        txtCodigo.setText(p.getCodigo());
        txtNome.setText(p.getNome());
        txtPrecoCompra.setText(p.getPrecoCompra().toPlainString());
        txtPrecoVenda.setText(p.getPrecoVenda().toPlainString());
        txtEstoqueMinimo.setText(String.valueOf(p.getEstoqueMinimo()));
        lblEstoqueAtual.setText(p.getEstoque() + " (definido pelas movimentações)");
        btnAtivarDesativar.setText(p.isAtivo() ? "Desativar" : "Ativar");

        for (Categoria c : cboCategoria.getItems()) {
            if (c.getId() == p.getIdCategoria()) {
                cboCategoria.setValue(c);
                break;
            }
        }
    }

    @FXML
    private void salvar() {
        lblMensagem.setText("");

        String codigo = txtCodigo.getText() == null ? "" : txtCodigo.getText().trim();
        String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
        Categoria categoria = cboCategoria.getValue();

        if (codigo.isEmpty() || nome.isEmpty() || categoria == null) {
            lblMensagem.setText("Código, nome e categoria são obrigatórios.");
            return;
        }

        BigDecimal precoCompra;
        BigDecimal precoVenda;
        int estoqueMinimo;

        try {
            precoCompra = new BigDecimal(txtPrecoCompra.getText().trim().replace(",", "."));
            precoVenda = new BigDecimal(txtPrecoVenda.getText().trim().replace(",", "."));
            estoqueMinimo = Integer.parseInt(txtEstoqueMinimo.getText().trim());
        } catch (NumberFormatException e) {
            lblMensagem.setText("Preços e estoque mínimo devem ser numéricos.");
            return;
        }

        if (precoCompra.compareTo(BigDecimal.ZERO) < 0) {
            lblMensagem.setText("O preço de compra não pode ser negativo.");
            return;
        }
        if (precoVenda.compareTo(BigDecimal.ZERO) <= 0) {
            lblMensagem.setText("O preço de venda deve ser maior que zero.");
            return;
        }
        if (estoqueMinimo < 0) {
            lblMensagem.setText("O estoque mínimo não pode ser negativo.");
            return;
        }

        try {
            if (produtoSelecionado == null) {
                Produto novoProduto = new Produto();
                novoProduto.setCodigo(codigo);
                novoProduto.setNome(nome);
                novoProduto.setIdCategoria(categoria.getId());
                novoProduto.setPrecoCompra(precoCompra);
                novoProduto.setPrecoVenda(precoVenda);
                novoProduto.setEstoqueMinimo(estoqueMinimo);
                novoProduto.setAtivo(true);
                produtoDAO.inserir(novoProduto);
            } else {
                produtoSelecionado.setCodigo(codigo);
                produtoSelecionado.setNome(nome);
                produtoSelecionado.setIdCategoria(categoria.getId());
                produtoSelecionado.setPrecoCompra(precoCompra);
                produtoSelecionado.setPrecoVenda(precoVenda);
                produtoSelecionado.setEstoqueMinimo(estoqueMinimo);
                produtoDAO.atualizar(produtoSelecionado);
            }
            novo();
            carregarProdutos();
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate")) {
                lblMensagem.setText("Já existe um produto com este código.");
            } else {
                lblMensagem.setText("Erro ao salvar produto: " + e.getMessage());
            }
        }
    }

    @FXML
    private void novo() {
        produtoSelecionado = null;
        txtCodigo.clear();
        txtNome.clear();
        txtPrecoCompra.clear();
        txtPrecoVenda.clear();
        txtEstoqueMinimo.clear();
        lblEstoqueAtual.setText("0 (definido pelas movimentações)");
        cboCategoria.setValue(null);
        btnAtivarDesativar.setText("Desativar");
        tabela.getSelectionModel().clearSelection();
    }

    @FXML
    private void alternarSituacao() {
        if (produtoSelecionado == null) {
            lblMensagem.setText("Selecione um produto para ativar/desativar.");
            return;
        }
        try {
            boolean novaSituacao = !produtoSelecionado.isAtivo();
            produtoDAO.alterarSituacao(produtoSelecionado.getId(), novaSituacao);
            novo();
            carregarProdutos();
        } catch (SQLException e) {
            lblMensagem.setText("Erro ao alterar situação: " + e.getMessage());
        }
    }

    @FXML
    private void pesquisar() {
        String termo = txtPesquisa.getText() == null ? "" : txtPesquisa.getText().trim();
        try {
            if (termo.isEmpty()) {
                dados.setAll(produtoDAO.listar());
            } else {
                dados.setAll(produtoDAO.pesquisar(termo));
            }
        } catch (SQLException e) {
            lblMensagem.setText("Erro na pesquisa: " + e.getMessage());
        }
    }

    @FXML
    private void listarTodos() {
        txtPesquisa.clear();
        carregarProdutos();
    }
}
