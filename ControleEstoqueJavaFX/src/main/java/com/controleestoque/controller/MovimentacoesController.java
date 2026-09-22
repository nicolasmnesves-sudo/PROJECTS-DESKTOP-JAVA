package com.controleestoque.controller;

import com.controleestoque.data.EstoqueException;
import com.controleestoque.data.MovimentacaoDAO;
import com.controleestoque.data.ProdutoDAO;
import com.controleestoque.model.Movimentacao;
import com.controleestoque.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MovimentacoesController {

    @FXML private ComboBox<Produto> cboProduto;
    @FXML private RadioButton rbEntrada;
    @FXML private RadioButton rbSaida;
    @FXML private TextField txtQuantidade;
    @FXML private TextArea txtObservacao;
    @FXML private Label lblEstoqueProdutoSelecionado;
    @FXML private Label lblMensagem;

    @FXML private ComboBox<Produto> cboFiltroProduto;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;

    @FXML private TableView<Movimentacao> tabela;
    @FXML private TableColumn<Movimentacao, String> colData;
    @FXML private TableColumn<Movimentacao, String> colProduto;
    @FXML private TableColumn<Movimentacao, String> colTipo;
    @FXML private TableColumn<Movimentacao, Integer> colQuantidade;
    @FXML private TableColumn<Movimentacao, String> colObservacao;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
    private final ObservableList<Movimentacao> dados = FXCollections.observableArrayList();
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        colData.setCellValueFactory(cellData -> {
            var data = cellData.getValue().getDataMovimentacao();
            return new javafx.beans.property.SimpleStringProperty(data == null ? "" : data.format(FORMATO_DATA));
        });
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        tabela.setItems(dados);

        cboProduto.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                lblEstoqueProdutoSelecionado.setText("Estoque atual: " + novo.getEstoque() + " unidade(s)");
            } else {
                lblEstoqueProdutoSelecionado.setText("");
            }
        });

        carregarProdutos();
        carregarHistorico();
    }

    private void carregarProdutos() {
        try {
            ObservableList<Produto> todos = FXCollections.observableArrayList(produtoDAO.listar());
            ObservableList<Produto> ativos = FXCollections.observableArrayList();
            for (Produto p : todos) {
                if (p.isAtivo()) {
                    ativos.add(p);
                }
            }
            cboProduto.setItems(ativos);
            cboProduto.setConverter(criarConversor());

            cboFiltroProduto.setItems(todos);
            cboFiltroProduto.setConverter(criarConversor());
        } catch (SQLException e) {
            lblMensagem.setText("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private StringConverter<Produto> criarConversor() {
        return new javafx.util.StringConverter<>() {
            @Override
            public String toString(Produto p) {
                return p == null ? "" : p.getCodigo() + " - " + p.getNome();
            }

            @Override
            public Produto fromString(String string) {
                return null;
            }
        };
    }

    private void carregarHistorico() {
        try {
            dados.setAll(movimentacaoDAO.listar());
        } catch (SQLException e) {
            lblMensagem.setText("Erro ao carregar histórico: " + e.getMessage());
        }
    }

    @FXML
    private void registrar() {
        lblMensagem.setText("");

        Produto produto = cboProduto.getValue();
        if (produto == null) {
            lblMensagem.setText("Selecione um produto.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(txtQuantidade.getText().trim());
        } catch (NumberFormatException e) {
            lblMensagem.setText("Informe uma quantidade numérica válida.");
            return;
        }

        Movimentacao mov = new Movimentacao();
        mov.setIdProduto(produto.getId());
        mov.setTipo(rbEntrada.isSelected() ? Movimentacao.ENTRADA : Movimentacao.SAIDA);
        mov.setQuantidade(quantidade);
        mov.setObservacao(txtObservacao.getText());

        try {
            // Registro em transação: valida estoque, insere histórico e atualiza saldo.
            // Se qualquer etapa falhar, um Rollback desfaz tudo automaticamente.
            movimentacaoDAO.registrar(mov);
            limparFormulario();
            carregarProdutos();
            carregarHistorico();
        } catch (EstoqueException e) {
            lblMensagem.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensagem.setText("Erro ao registrar movimentação: " + e.getMessage());
        }
    }

    @FXML
    private void limparFormulario() {
        cboProduto.setValue(null);
        rbEntrada.setSelected(true);
        txtQuantidade.clear();
        txtObservacao.clear();
        lblEstoqueProdutoSelecionado.setText("");
    }

    @FXML
    private void filtrar() {
        lblMensagem.setText("");
        Produto produtoFiltro = cboFiltroProduto.getValue();
        LocalDate inicio = dpInicio.getValue();
        LocalDate fim = dpFim.getValue();

        try {
            if (produtoFiltro != null) {
                dados.setAll(movimentacaoDAO.listarPorProduto(produtoFiltro.getId()));
            } else if (inicio != null && fim != null) {
                dados.setAll(movimentacaoDAO.listarPorPeriodo(inicio, fim));
            } else {
                lblMensagem.setText("Selecione um produto ou informe o período (início e fim).");
            }
        } catch (SQLException e) {
            lblMensagem.setText("Erro ao filtrar: " + e.getMessage());
        }
    }

    @FXML
    private void limparFiltro() {
        cboFiltroProduto.setValue(null);
        dpInicio.setValue(null);
        dpFim.setValue(null);
        carregarHistorico();
    }
}
