package nicolasmneves.gerenciamentodeestoque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

public class EstoqueController {

    // --- CAMPOS DA INTERFACE (FXML) ---
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextField txtPreco;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtPesquisa;

    // --- TABELA E COLUNAS ---
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, Integer> colCodigo;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, Double> colPreco;
    @FXML private TableColumn<Produto, Integer> colQuantidade;
    @FXML private TableColumn<Produto, Double> colValorEmEstoque;

    // --- LISTA EM MEMÓRIA ---
    private final ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();
    private FilteredList<Produto> listaFiltrada;

    // Formatador de Moeda Brasileira (R$)
    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        // 1. Preencher opções do ComboBox de Categoria
        cmbCategoria.setItems(FXCollections.observableArrayList(
                "Selecione uma categoria",
                "Informática",
                "Periféricos",
                "Eletrônicos",
                "Escritório",
                "Acessórios",
                "Outros"
        ));
        cmbCategoria.getSelectionModel().selectFirst();

        // 2. Mapear as colunas da tabela com os atributos do Produto
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colValorEmEstoque.setCellValueFactory(new PropertyValueFactory<>("valorEmEstoque"));

        // 3. Formatar exibição de preço e valor total no formato R$
        formatarColunaMoeda(colPreco);
        formatarColunaMoeda(colValorEmEstoque);

        // 4. Configurar filtro de pesquisa e vincular a lista à tabela
        listaFiltrada = new FilteredList<>(listaProdutos, p -> true);
        tabelaProdutos.setItems(listaFiltrada);

        // 5. Listener: Quando o usuário clica em uma linha da tabela, carrega nos campos
        tabelaProdutos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, produtoSelecionado) -> carregarProdutoNosCampos(produtoSelecionado)
        );
    }

    // --- AÇÕES DOS BOTÕES (CRUD) ---

    @FXML
    private void onAdicionarClick() {
        if (!validarCampos()) return;

        int codigo = Integer.parseInt(txtCodigo.getText().trim());

        // Regra RN02: Código único
        if (produtoJaCadastrado(codigo, null)) {
            mostrarAlerta("Erro de Validação", "Já existe um produto cadastrado com este código.", Alert.AlertType.WARNING);
            return;
        }

        String nome = txtNome.getText().trim();
        String categoria = cmbCategoria.getValue();
        double preco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));
        int quantidade = Integer.parseInt(txtQuantidade.getText().trim());

        // Criar o novo objeto e adicionar à lista
        Produto novoProduto = new Produto(codigo, nome, categoria, preco, quantidade);
        listaProdutos.add(novoProduto);

        mostrarAlerta("Sucesso", "Produto cadastrado com sucesso!", Alert.AlertType.INFORMATION);
        limparCampos();
    }

    @FXML
    private void onAtualizarClick() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        // Regra RN07: Atualizar somente com seleção
        if (produtoSelecionado == null) {
            mostrarAlerta("Aviso", "Selecione um produto na tabela para atualizar.", Alert.AlertType.WARNING);
            return;
        }

        if (!validarCampos()) return;

        int novoCodigo = Integer.parseInt(txtCodigo.getText().trim());

        // Se o código mudou, verificar se o novo código já existe em outro produto
        if (novoCodigo != produtoSelecionado.getCodigo() && produtoJaCadastrado(novoCodigo, produtoSelecionado)) {
            mostrarAlerta("Erro de Validação", "Já existe outro produto cadastrado com este novo código.", Alert.AlertType.WARNING);
            return;
        }

        // Atualizar as propriedades do objeto existente
        produtoSelecionado.setCodigo(novoCodigo);
        produtoSelecionado.setNome(txtNome.getText().trim());
        produtoSelecionado.setCategoria(cmbCategoria.getValue());
        produtoSelecionado.setPreco(Double.parseDouble(txtPreco.getText().trim().replace(",", ".")));
        produtoSelecionado.setQuantidade(Integer.parseInt(txtQuantidade.getText().trim()));

        tabelaProdutos.refresh(); // Força a atualização visual da tabela
        mostrarAlerta("Sucesso", "Produto atualizado com sucesso!", Alert.AlertType.INFORMATION);
        limparCampos();
    }

    @FXML
    private void onExcluirClick() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        // Regra RN08: Excluir somente com seleção
        if (produtoSelecionado == null) {
            mostrarAlerta("Aviso", "Selecione um produto na tabela para excluir.", Alert.AlertType.WARNING);
            return;
        }

        // Regra RN09: Confirmação antes de excluir
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText(null);
        alert.setContentText("Deseja realmente excluir o produto " + produtoSelecionado.getNome() + "?");

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            listaProdutos.remove(produtoSelecionado);
            mostrarAlerta("Sucesso", "Produto excluído com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
        }
    }

    @FXML
    private void onPesquisarClick() {
        String termo = txtPesquisa.getText().toLowerCase().trim();

        if (termo.isEmpty()) {
            listaFiltrada.setPredicate(p -> true);
            return;
        }

        // Regra RN11: Pesquisa por Código, Nome ou Categoria (ignora maiúsculas/minúsculas)
        listaFiltrada.setPredicate(produto ->
                String.valueOf(produto.getCodigo()).contains(termo) ||
                        produto.getNome().toLowerCase().contains(termo) ||
                        produto.getCategoria().toLowerCase().contains(termo)
        );

        if (listaFiltrada.isEmpty()) {
            mostrarAlerta("Pesquisa", "Nenhum produto foi encontrado.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void onLimparClick() {
        limparCampos();
    }

    @FXML
    private void onSairClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Encerrar Aplicação");
        alert.setHeaderText(null);
        alert.setContentText("Deseja realmente encerrar o sistema?");

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Stage stage = (Stage) txtCodigo.getScene().getWindow();
            stage.close();
        }
    }

    // --- MÉTODOS AUXILIARES E VALIDAÇÕES ---

    private boolean validarCampos() {
        // Validar Código
        if (txtCodigo.getText().trim().isEmpty()) {
            mostrarAlerta("Validação", "Informe um código numérico válido.", Alert.AlertType.WARNING);
            return false;
        }
        try {
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            if (codigo <= 0) {
                mostrarAlerta("Validação", "O código deve ser maior que zero.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Validação", "O código deve conter apenas números inteiros.", Alert.AlertType.WARNING);
            return false;
        }

        // Validar Nome
        if (txtNome.getText().trim().isEmpty()) {
            mostrarAlerta("Validação", "Digite o nome do produto.", Alert.AlertType.WARNING);
            return false;
        }

        // Validar Categoria
        if (cmbCategoria.getValue() == null || cmbCategoria.getValue().equals("Selecione uma categoria")) {
            mostrarAlerta("Validação", "Selecione uma categoria válida.", Alert.AlertType.WARNING);
            return false;
        }

        // Validar Preço (RN05)
        if (txtPreco.getText().trim().isEmpty()) {
            mostrarAlerta("Validação", "Informe o preço do produto.", Alert.AlertType.WARNING);
            return false;
        }
        try {
            double preco = Double.parseDouble(txtPreco.getText().trim().replace(",", "."));
            if (preco <= 0) {
                mostrarAlerta("Validação", "Informe um preço maior que zero.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Validação", "Informe um preço numérico válido (ex: 89.90).", Alert.AlertType.WARNING);
            return false;
        }

        // Validar Quantidade (RN06)
        if (txtQuantidade.getText().trim().isEmpty()) {
            mostrarAlerta("Validação", "Informe a quantidade em estoque.", Alert.AlertType.WARNING);
            return false;
        }
        try {
            int qtd = Integer.parseInt(txtQuantidade.getText().trim());
            if (qtd < 0) {
                mostrarAlerta("Validação", "A quantidade não pode ser negativa.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Validação", "A quantidade deve ser um número inteiro válido.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private boolean produtoJaCadastrado(int codigo, Produto produtoAtual) {
        return listaProdutos.stream()
                .anyMatch(p -> p.getCodigo() == codigo && p != produtoAtual);
    }

    private void carregarProdutoNosCampos(Produto produto) {
        if (produto != null) {
            txtCodigo.setText(String.valueOf(produto.getCodigo()));
            txtNome.setText(produto.getNome());
            cmbCategoria.setValue(produto.getCategoria());
            txtPreco.setText(String.format(Locale.US, "%.2f", produto.getPreco()));
            txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
        }
    }

    private void limparCampos() {
        txtCodigo.clear();
        txtNome.clear();
        cmbCategoria.getSelectionModel().selectFirst();
        txtPreco.clear();
        txtQuantidade.clear();
        txtPesquisa.clear();

        tabelaProdutos.getSelectionModel().clearSelection();
        listaFiltrada.setPredicate(p -> true); // Restaura todos os registros na grade
        txtCodigo.requestFocus(); // Posiciona o cursor no primeiro campo
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void formatarColunaMoeda(TableColumn<Produto, Double> coluna) {
        coluna.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatoMoeda.format(item));
                }
            }
        });
    }
}