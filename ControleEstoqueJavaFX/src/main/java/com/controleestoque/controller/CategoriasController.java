package com.controleestoque.controller;

import com.controleestoque.data.CategoriaDAO;
import com.controleestoque.model.Categoria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CategoriasController {

    @FXML private TextField txtNome;
    @FXML private Label lblMensagem;
    @FXML private TableView<Categoria> tabela;
    @FXML private TableColumn<Categoria, Integer> colId;
    @FXML private TableColumn<Categoria, String> colNome;

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ObservableList<Categoria> dados = FXCollections.observableArrayList();
    private Categoria categoriaSelecionada;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tabela.setItems(dados);

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            categoriaSelecionada = novo;
            if (novo != null) {
                txtNome.setText(novo.getNome());
            }
        });

        carregar();
    }

    private void carregar() {
        try {
            dados.setAll(categoriaDAO.listar());
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    @FXML
    private void salvar() {
        String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
        if (nome.isEmpty()) {
            lblMensagem.setText("Informe o nome da categoria.");
            return;
        }

        try {
            if (categoriaSelecionada == null) {
                categoriaDAO.inserir(new Categoria(nome));
            } else {
                categoriaSelecionada.setNome(nome);
                categoriaDAO.atualizar(categoriaSelecionada);
            }
            lblMensagem.setText("");
            novo();
            carregar();
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar categoria: " + e.getMessage());
        }
    }

    @FXML
    private void novo() {
        categoriaSelecionada = null;
        txtNome.clear();
        tabela.getSelectionModel().clearSelection();
    }

    @FXML
    private void excluir() {
        if (categoriaSelecionada == null) {
            lblMensagem.setText("Selecione uma categoria para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja realmente excluir a categoria \"" + categoriaSelecionada.getNome() + "\"?");
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                try {
                    categoriaDAO.excluir(categoriaSelecionada.getId());
                    novo();
                    carregar();
                } catch (SQLException e) {
                    mostrarErro("Não foi possível excluir: verifique se não há produtos vinculados a esta categoria.");
                }
            }
        });
    }

    private void mostrarErro(String mensagem) {
        lblMensagem.setText(mensagem);
    }
}
