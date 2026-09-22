package nicolasmneves.sistemacadastroclientes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller da tela "Cadastro de Clientes".
 *
 * O TableView (tabelaClientes) faz o papel do DataGridView do exemplo original em C#.
 * Todas as operações são persistidas no MySQL através do ClienteDAO.
 */
public class ClientesController implements Initializable {

    private static final DateTimeFormatter FORMATADOR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ----- Dados -----
    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField emailField;
    @FXML private TextField telefoneField;
    @FXML private TextField cidadeField;
    @FXML private CheckBox ativoCheck;

    // ----- Pesquisa -----
    @FXML private TextField pesquisaField;
    @FXML private Button pesquisarButton;
    @FXML private Button mostrarTodosButton;

    // ----- Tabela -----
    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Number> colId;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colCpf;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colTelefone;
    @FXML private TableColumn<Cliente, String> colCidade;
    @FXML private TableColumn<Cliente, Boolean> colSituacao;
    @FXML private TableColumn<Cliente, LocalDateTime> colDataCadastro;

    // ----- Ações -----
    @FXML private Button novoButton;
    @FXML private Button salvarButton;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;
    @FXML private Button ativarDesativarButton;
    @FXML private Button cancelarButton;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idField.setEditable(false);

        configurarColunas();
        tabelaClientes.setItems(clientes);

        tabelaClientes.getSelectionModel().selectedItemProperty()
                .addListener((observable, antigo, selecionado) -> carregarCamposComCliente(selecionado));

        recarregarTabela();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));

        colSituacao.setCellValueFactory(new PropertyValueFactory<>("ativo"));
        colSituacao.setCellFactory(coluna -> new TableCell<Cliente, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (item ? "Ativo" : "Inativo"));
            }
        });

        colDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
        colDataCadastro.setCellFactory(coluna -> new TableCell<Cliente, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(FORMATADOR_DATA_HORA));
            }
        });
    }

    // ---------------------------------------------------------------
    // Botão: Novo
    // ---------------------------------------------------------------
    @FXML
    private void handleNovo() {
        tabelaClientes.getSelectionModel().clearSelection();
        limparCampos();
        ativoCheck.setSelected(true);
        nomeField.requestFocus();
    }

    // ---------------------------------------------------------------
    // Botão: Salvar (inserir novo cliente)
    // ---------------------------------------------------------------
    @FXML
    private void handleSalvar() {
        if (!validarCampos()) {
            return;
        }

        String cpf = cpfField.getText().trim();

        try {
            if (clienteDAO.cpfExistente(cpf, null)) {
                mostrarErro("CPF já cadastrado.");
                return;
            }

            Cliente cliente = new Cliente(
                    nomeField.getText().trim(),
                    cpf,
                    emailField.getText() == null ? "" : emailField.getText().trim(),
                    telefoneField.getText() == null ? "" : telefoneField.getText().trim(),
                    cidadeField.getText() == null ? "" : cidadeField.getText().trim(),
                    ativoCheck.isSelected()
            );

            clienteDAO.inserir(cliente);
            recarregarTabela();
            limparCampos();
            mostrarInfo("Cliente cadastrado com sucesso.");
        } catch (SQLException e) {
            mostrarErro("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Botão: Editar (atualiza o registro selecionado pelo ID)
    // ---------------------------------------------------------------
    @FXML
    private void handleEditar() {
        Cliente selecionado = obterClienteSelecionado();
        if (selecionado == null) {
            return;
        }

        if (!validarCampos()) {
            return;
        }

        String cpf = cpfField.getText().trim();

        try {
            if (clienteDAO.cpfExistente(cpf, selecionado.getId())) {
                mostrarErro("CPF já cadastrado.");
                return;
            }

            selecionado.setNome(nomeField.getText().trim());
            selecionado.setCpf(cpf);
            selecionado.setEmail(emailField.getText() == null ? "" : emailField.getText().trim());
            selecionado.setTelefone(telefoneField.getText() == null ? "" : telefoneField.getText().trim());
            selecionado.setCidade(cidadeField.getText() == null ? "" : cidadeField.getText().trim());
            selecionado.setAtivo(ativoCheck.isSelected());

            clienteDAO.atualizar(selecionado);
            recarregarTabela();
            limparCampos();
            mostrarInfo("Cliente atualizado com sucesso.");
        } catch (SQLException e) {
            mostrarErro("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Botão: Ativar/Desativar
    // ---------------------------------------------------------------
    @FXML
    private void handleAtivarDesativar() {
        Cliente selecionado = obterClienteSelecionado();
        if (selecionado == null) {
            return;
        }

        boolean novaSituacao = !selecionado.isAtivo();

        try {
            clienteDAO.alterarSituacao(selecionado.getId(), novaSituacao);
            recarregarTabela();
            limparCampos();
            mostrarInfo(novaSituacao ? "Cliente ativado com sucesso." : "Cliente desativado com sucesso.");
        } catch (SQLException e) {
            mostrarErro("Erro ao alterar a situação do cliente: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Botão: Excluir
    // ---------------------------------------------------------------
    @FXML
    private void handleExcluir() {
        Cliente selecionado = obterClienteSelecionado();
        if (selecionado == null) {
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir o cliente selecionado?");

        confirmacao.showAndWait().ifPresent(botao -> {
            if (botao == ButtonType.OK) {
                try {
                    clienteDAO.excluir(selecionado.getId());
                    recarregarTabela();
                    limparCampos();
                    mostrarInfo("Cliente excluído com sucesso.");
                } catch (SQLException e) {
                    mostrarErro("Erro ao excluir cliente: " + e.getMessage());
                }
            }
        });
    }

    // ---------------------------------------------------------------
    // Botão: Pesquisar / Mostrar Todos
    // ---------------------------------------------------------------
    @FXML
    private void handlePesquisar() {
        String texto = pesquisaField.getText() == null ? "" : pesquisaField.getText().trim();

        try {
            if (texto.isEmpty()) {
                clientes.setAll(clienteDAO.listar());
            } else {
                clientes.setAll(clienteDAO.pesquisarPorNome(texto));
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao pesquisar clientes: " + e.getMessage());
        }
    }

    @FXML
    private void handleMostrarTodos() {
        pesquisaField.clear();
        recarregarTabela();
    }

    // ---------------------------------------------------------------
    // Botão: Cancelar
    // ---------------------------------------------------------------
    @FXML
    private void handleCancelar() {
        tabelaClientes.getSelectionModel().clearSelection();
        limparCampos();
    }

    // ---------------------------------------------------------------
    // Auxiliares
    // ---------------------------------------------------------------
    private Cliente obterClienteSelecionado() {
        Cliente selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarErro("Selecione um cliente na tabela.");
            return null;
        }
        return selecionado;
    }

    private void carregarCamposComCliente(Cliente cliente) {
        if (cliente == null) {
            return;
        }
        idField.setText(String.valueOf(cliente.getId()));
        nomeField.setText(cliente.getNome());
        cpfField.setText(cliente.getCpf());
        emailField.setText(cliente.getEmail());
        telefoneField.setText(cliente.getTelefone());
        cidadeField.setText(cliente.getCidade());
        ativoCheck.setSelected(cliente.isAtivo());
    }

    private boolean validarCampos() {
        if (nomeField.getText() == null || nomeField.getText().trim().isEmpty()) {
            mostrarErro("Informe o nome do cliente.");
            nomeField.requestFocus();
            return false;
        }
        if (cpfField.getText() == null || cpfField.getText().trim().isEmpty()) {
            mostrarErro("Informe o CPF do cliente.");
            cpfField.requestFocus();
            return false;
        }
        String email = emailField.getText();
        if (email != null && !email.trim().isEmpty() && !email.contains("@")) {
            mostrarErro("O e-mail informado é inválido.");
            emailField.requestFocus();
            return false;
        }
        return true;
    }

    private void limparCampos() {
        idField.clear();
        nomeField.clear();
        cpfField.clear();
        emailField.clear();
        telefoneField.clear();
        cidadeField.clear();
        ativoCheck.setSelected(true);
    }

    private void recarregarTabela() {
        try {
            clientes.setAll(clienteDAO.listar());
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void mostrarInfo(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Sucesso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
