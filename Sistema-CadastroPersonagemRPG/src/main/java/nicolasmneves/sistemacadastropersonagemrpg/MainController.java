package nicolasmneves.sistemacadastropersonagemrpg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainController {

    @FXML private TextField txtName, txtClass, txtRace, txtOrigin, txtSearch;
    @FXML private Spinner<Integer> spnFOR, spnDEF, spnINT, spnAGI;
    @FXML private TableView<Personagem> tableView;
    @FXML private TableColumn<Personagem, String> colName, colClass, colRace, colOrigin;
    @FXML private TableColumn<Personagem, Integer> colFOR, colDEF, colAGI, colINT;
    @FXML private Button bntNew, bntSave, bntUpdate, bntDelete, bntEdit, bntSearch, bntImage;
    @FXML private ImageView imagem;

    private final ObservableList<Personagem> listaPersonagens = FXCollections.observableArrayList();
    private FilteredList<Personagem> listaFiltrada;
    private final String ARQUIVO_CSV = "personagens.csv";
    private String caminhoImagemAtual = "";

    @FXML
    public void initialize() {
        //  Configura os Spinners de atributos (de 0 a 100, valor padrão 10)
        spnFOR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        spnDEF.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        spnINT.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        spnAGI.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));

        //  Mapeia as colunas da Tabela
        colName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("classe"));
        colRace.setCellValueFactory(new PropertyValueFactory<>("raca"));
        colOrigin.setCellValueFactory(new PropertyValueFactory<>("origem"));
        colFOR.setCellValueFactory(new PropertyValueFactory<>("forca"));
        colDEF.setCellValueFactory(new PropertyValueFactory<>("defesa"));
        colAGI.setCellValueFactory(new PropertyValueFactory<>("agilidade"));
        colINT.setCellValueFactory(new PropertyValueFactory<>("inteligencia"));

        //  Configura a lista filtrada para pesquisa
        listaFiltrada = new FilteredList<>(listaPersonagens, p -> true);
        tableView.setItems(listaFiltrada);

        //  Carrega os dados salvos no CSV
        carregarDadosCsv();

        //  Associa os botões aos métodos
        bntNew.setOnAction(e -> limparCampos());
        bntSave.setOnAction(e -> salvarNovo());
        bntEdit.setOnAction(e -> carregarParaEdicao());
        bntUpdate.setOnAction(e -> atualizarRegistro());
        bntDelete.setOnAction(e -> excluirRegistro());
        bntSearch.setOnAction(e -> realizarPesquisa());
        bntImage.setOnAction(e -> selecionarImagem());

        // Pesquisa em tempo real ao digitar
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> realizarPesquisa());
    }

    private void salvarNovo() {
        if (!validarCampos()) return;

        Personagem p = criarObjetoDosCampos();
        listaPersonagens.add(p);
        salvarDadosCsv();
        limparCampos();
        exibirAlerta("Sucesso", "Personagem cadastrado com sucesso!", Alert.AlertType.INFORMATION);
    }

    private void carregarParaEdicao() {
        Personagem selecionado = tableView.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            exibirAlerta("Aviso", "Selecione um personagem na tabela para editar.", Alert.AlertType.WARNING);
            return;
        }

        txtName.setText(selecionado.getNome());
        txtClass.setText(selecionado.getClasse());
        txtRace.setText(selecionado.getRaca());
        txtOrigin.setText(selecionado.getOrigem());
        spnFOR.getValueFactory().setValue(selecionado.getForca());
        spnDEF.getValueFactory().setValue(selecionado.getDefesa());
        spnAGI.getValueFactory().setValue(selecionado.getAgilidade());
        spnINT.getValueFactory().setValue(selecionado.getInteligencia());
        caminhoImagemAtual = selecionado.getCaminhoImagem();

        carregarImagemNaView(caminhoImagemAtual);
    }

    private void atualizarRegistro() {
        Personagem selecionado = tableView.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            exibirAlerta("Aviso", "Selecione um personagem na tabela para atualizar.", Alert.AlertType.WARNING);
            return;
        }

        if (!validarCampos()) return;

        selecionado.setNome(txtName.getText());
        selecionado.setClasse(txtClass.getText());
        selecionado.setRaca(txtRace.getText());
        selecionado.setOrigem(txtOrigin.getText());
        selecionado.setForca(spnFOR.getValue());
        selecionado.setDefesa(spnDEF.getValue());
        selecionado.setAgilidade(spnAGI.getValue());
        selecionado.setInteligencia(spnINT.getValue());
        selecionado.setCaminhoImagem(caminhoImagemAtual);

        tableView.refresh();
        salvarDadosCsv();
        limparCampos();
        exibirAlerta("Sucesso", "Personagem atualizado com sucesso!", Alert.AlertType.INFORMATION);
    }

    private void excluirRegistro() {
        Personagem selecionado = tableView.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            exibirAlerta("Aviso", "Selecione um personagem para excluir.", Alert.AlertType.WARNING);
            return;
        }

        listaPersonagens.remove(selecionado);
        salvarDadosCsv();
        limparCampos();
        exibirAlerta("Sucesso", "Personagem removido com sucesso!", Alert.AlertType.INFORMATION);
    }

    private void realizarPesquisa() {
        String termo = txtSearch.getText().toLowerCase().trim();
        listaFiltrada.setPredicate(personagem -> {
            if (termo.isEmpty()) return true;
            return personagem.getNome().toLowerCase().contains(termo) ||
                    personagem.getClasse().toLowerCase().contains(termo) ||
                    personagem.getRaca().toLowerCase().contains(termo);
        });
    }

    private void selecionarImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Imagem do Personagem");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            caminhoImagemAtual = file.toURI().toString();
            carregarImagemNaView(caminhoImagemAtual);
        }
    }

    private void carregarImagemNaView(String url) {
        if (url != null && !url.isEmpty()) {
            try {
                imagem.setImage(new Image(url));
            } catch (Exception e) {
                imagem.setImage(null);
            }
        } else {
            imagem.setImage(null);
        }
    }

    private void limparCampos() {
        txtName.clear();
        txtClass.clear();
        txtRace.clear();
        txtOrigin.clear();
        spnFOR.getValueFactory().setValue(10);
        spnDEF.getValueFactory().setValue(10);
        spnAGI.getValueFactory().setValue(10);
        spnINT.getValueFactory().setValue(10);
        caminhoImagemAtual = "";
        imagem.setImage(null);
        tableView.getSelectionModel().clearSelection();
    }

    private Personagem criarObjetoDosCampos() {
        return new Personagem(
                txtName.getText(),
                txtClass.getText(),
                txtRace.getText(),
                txtOrigin.getText(),
                spnFOR.getValue(),
                spnDEF.getValue(),
                spnAGI.getValue(),
                spnINT.getValue(),
                caminhoImagemAtual
        );
    }

    private boolean validarCampos() {
        if (txtName.getText().isBlank() || txtClass.getText().isBlank() ||
                txtRace.getText().isBlank() || txtOrigin.getText().isBlank()) {
            exibirAlerta("Validação", "Preencha todos os campos de texto!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    // --- Persistência em Arquivo CSV ---

    private void salvarDadosCsv() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARQUIVO_CSV), StandardCharsets.UTF_8)) {
            for (Personagem p : listaPersonagens) {
                writer.write(p.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            exibirAlerta("Erro", "Erro ao salvar no arquivo CSV: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void carregarDadosCsv() {
        File arq = new File(ARQUIVO_CSV);
        if (!arq.exists()) return;

        listaPersonagens.clear();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(ARQUIVO_CSV), StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.isBlank()) {
                    Personagem p = Personagem.fromCsv(linha);
                    if (p != null) {
                        listaPersonagens.add(p);
                    }
                }
            }
        } catch (IOException e) {
            exibirAlerta("Erro", "Erro ao carregar o arquivo CSV: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}