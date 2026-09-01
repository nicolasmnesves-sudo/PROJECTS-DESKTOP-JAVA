package com.controleestoque.controller;

import com.controleestoque.data.MovimentacaoDAO;
import com.controleestoque.data.ProdutoDAO;
import com.controleestoque.model.Movimentacao;
import com.controleestoque.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrincipalController {

    @FXML private Label lblTotalProdutos;
    @FXML private Label lblProdutosAtivos;
    @FXML private Label lblTotalItens;
    @FXML private Label lblEstoqueBaixo;
    @FXML private Label lblValorEstoque;

    @FXML private TableView<Produto> tabelaEstoqueBaixo;
    @FXML private TableColumn<Produto, String> colBaixoCodigo;
    @FXML private TableColumn<Produto, String> colBaixoNome;
    @FXML private TableColumn<Produto, Integer> colBaixoEstoque;
    @FXML private TableColumn<Produto, Integer> colBaixoMinimo;

    @FXML private TableView<Movimentacao> tabelaUltimasMovimentacoes;
    @FXML private TableColumn<Movimentacao, String> colMovData;
    @FXML private TableColumn<Movimentacao, String> colMovProduto;
    @FXML private TableColumn<Movimentacao, String> colMovTipo;
    @FXML private TableColumn<Movimentacao, Integer> colMovQuantidade;
    @FXML private TableColumn<Movimentacao, String> colMovObservacao;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        colBaixoCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colBaixoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colBaixoEstoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colBaixoMinimo.setCellValueFactory(new PropertyValueFactory<>("estoqueMinimo"));

        colMovData.setCellValueFactory(cellData -> {
            var data = cellData.getValue().getDataMovimentacao();
            return new javafx.beans.property.SimpleStringProperty(data == null ? "" : data.format(FORMATO_DATA));
        });
        colMovProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colMovTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMovQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colMovObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));

        atualizarPainel();
    }

    @FXML
    private void atualizarPainel() {
        try {
            List<Produto> todos = produtoDAO.listar();
            List<Produto> baixoEstoque = produtoDAO.listarEstoqueBaixo();
            List<Movimentacao> ultimas = movimentacaoDAO.listarUltimas(10);

            long ativos = todos.stream().filter(Produto::isAtivo).count();
            int totalItens = todos.stream().mapToInt(Produto::getEstoque).sum();
            BigDecimal valorEstoque = todos.stream()
                    .map(p -> p.getPrecoCompra().multiply(BigDecimal.valueOf(p.getEstoque())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            lblTotalProdutos.setText(String.valueOf(todos.size()));
            lblProdutosAtivos.setText(String.valueOf(ativos));
            lblTotalItens.setText(String.valueOf(totalItens));
            lblEstoqueBaixo.setText(String.valueOf(baixoEstoque.size()));
            lblValorEstoque.setText(String.format("R$ %,.2f", valorEstoque));

            tabelaEstoqueBaixo.setItems(FXCollections.observableArrayList(baixoEstoque));
            tabelaUltimasMovimentacoes.setItems(FXCollections.observableArrayList(ultimas));

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao carregar o painel: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void abrirCategorias() {
        abrirJanela("/fxml/categorias.fxml", "Categorias");
    }

    @FXML
    private void abrirProdutos() {
        abrirJanela("/fxml/produtos.fxml", "Produtos");
    }

    @FXML
    private void abrirMovimentacoes() {
        abrirJanela("/fxml/movimentacoes.fxml", "Movimentações");
    }

    private void abrirJanela(String caminhoFxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.setOnHidden(e -> atualizarPainel());
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao abrir a tela " + titulo + ": " + e.getMessage()).showAndWait();
        }
    }
}
