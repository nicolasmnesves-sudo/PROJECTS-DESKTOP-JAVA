package nicolasmneves.estacionamento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class EstacionamentoController {

    @FXML private TextField txtPlaca, txtModelo, txtPesquisarPlaca, txtHoraEntrada, txtMinEntrada, txtHoraSaida, txtMinSaida;
    @FXML private ComboBox<String> cboTipoVeiculo;
    @FXML private DatePicker dpEntrada, dpSaida;
    @FXML private Label lblEstacionados, lblFinalizados, lblTotalRecebido;

    @FXML private TableView<Permanencia> dgvVeiculos;
    @FXML private TableColumn<Permanencia, String> colPlaca, colModelo, colTipo, colSituacao;
    @FXML private TableColumn<Permanencia, LocalDateTime> colEntrada, colSaida;
    @FXML private TableColumn<Permanencia, Integer> colHoras;
    @FXML private TableColumn<Permanencia, Double> colValor;

    // Coleções para o TableView e Filtro
    private final ObservableList<Permanencia> listaVeiculos = FXCollections.observableArrayList();
    private FilteredList<Permanencia> dadosFiltrados;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        // Passo 3 - Configurar ComboBox e Datas Iniciais
        cboTipoVeiculo.getItems().addAll("Moto", "Carro", "Utilitário");
        resetarDatas();

        // Configuração das colunas (Passos 1 e 2)
        colPlaca.setCellValueFactory(cellData -> cellData.getValue().placaProperty());
        colModelo.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colHoras.setCellValueFactory(cellData -> cellData.getValue().horasProperty().asObject());
        colSituacao.setCellValueFactory(cellData -> cellData.getValue().situacaoProperty());

        // Passo 2 (60 e 61) - Formatação de Data e Valor (Moeda)
        colEntrada.setCellValueFactory(cellData -> cellData.getValue().entradaProperty());
        colEntrada.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : formatter.format(item));
            }
        });

        colSaida.setCellValueFactory(cellData -> cellData.getValue().saidaProperty());
        colSaida.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : formatter.format(item));
            }
        });

        colValor.setCellValueFactory(cellData -> cellData.getValue().valorProperty().asObject());
        colValor.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("R$ %.2f", item));
            }
        });

        // Passo 14 - Colorir Célula da Situação
        colSituacao.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("Estacionado")) {
                        setStyle("-fx-background-color: #fffae6; -fx-text-fill: #b38600;"); // Amarelo Claro
                    } else {
                        setStyle("-fx-background-color: #e6f9ec; -fx-text-fill: #00802b;"); // Verde Claro
                    }
                }
            }
        });

        // Envelopando lista para permitir filtragem posterior
        dadosFiltrados = new FilteredList<>(listaVeiculos, p -> true);
        dgvVeiculos.setItems(dadosFiltrados);
    }

    private void resetarDatas() {
        LocalDateTime agora = LocalDateTime.now();
        dpEntrada.setValue(agora.toLocalDate());
        txtHoraEntrada.setText(String.format("%02d", agora.getHour()));
        txtMinEntrada.setText(String.format("%02d", agora.getMinute()));

        dpSaida.setValue(agora.toLocalDate());
        txtHoraSaida.setText(String.format("%02d", agora.getHour()));
        txtMinSaida.setText(String.format("%02d", agora.getMinute()));
    }

    // Passo 4 - Validar Entrada
    private boolean validarEntrada() {
        if (txtPlaca.getText().trim().isEmpty()) {
            mostrarAlerta("Informe a placa do veículo.");
            txtPlaca.requestFocus();
            return false;
        }
        if (txtModelo.getText().trim().isEmpty()) {
            mostrarAlerta("Informe o modelo do veículo.");
            txtModelo.requestFocus();
            return false;
        }
        if (cboTipoVeiculo.getSelectionModel().isEmpty()) {
            mostrarAlerta("Selecione o tipo do veículo.");
            return false;
        }
        return true;
    }

    // Passo 5 - Verificar placa estacionada
    private boolean placaJaEstacionada(String placa) {
        return listaVeiculos.stream()
                .anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa) && v.getSituacao().equals("Estacionado"));
    }

    // Passo 6 - Registrar Entrada
    @FXML
    private void btnRegistrarEntradaClick() {
        if (!validarEntrada()) return;

        String placa = txtPlaca.getText().trim().toUpperCase();
        if (placaJaEstacionada(placa)) {
            mostrarAlerta("Esta placa já possui uma entrada em aberto.");
            return;
        }

        LocalDate data = dpEntrada.getValue();
        int hora = Integer.parseInt(txtHoraEntrada.getText().trim());
        int min = Integer.parseInt(txtMinEntrada.getText().trim());
        LocalDateTime dataEntrada = LocalDateTime.of(data, LocalTime.of(hora, min));

        Permanencia novo = new Permanencia(placa, txtModelo.getText().trim(), cboTipoVeiculo.getValue(), dataEntrada);
        listaVeiculos.add(novo);

        atualizarIndicadores();
        limparCampos();
    }

    // Passo 7 - Obter Linha Selecionada
    private Permanencia obterLinhaSelecionada() {
        Permanencia selecionado = dgvVeiculos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione uma linha na tabela.");
        }
        return selecionado;
    }

    // Passo 8 - Calcular horas e valor
    private double obterValorHora(String tipo) {
        switch (tipo) {
            case "Moto": return 4.0;
            case "Carro": return 7.0;
            default: return 10.0;
        }
    }

    private int calcularHoras(LocalDateTime entrada, LocalDateTime saida) {
        long minutos = ChronoUnit.MINUTES.between(entrada, saida);
        return (int) Math.ceil(minutos / 60.0); // Arredondamento para cima
    }

    // Passo 9 - Registrar Saída
    @FXML
    private void btnRegistrarSaidaClick() {
        Permanencia selecionado = obterLinhaSelecionada();
        if (selecionado == null) return;

        if (selecionado.getSituacao().equals("Finalizado")) {
            mostrarAlerta("A saída deste veículo já foi registrada.");
            return;
        }

        LocalDate dataS = dpSaida.getValue();
        int horaS = Integer.parseInt(txtHoraSaida.getText().trim());
        int minS = Integer.parseInt(txtMinSaida.getText().trim());
        LocalDateTime dataSaida = LocalDateTime.of(dataS, LocalTime.of(horaS, minS));

        if (dataSaida.isBefore(selecionado.getEntrada())) {
            mostrarAlerta("A saída não pode ser anterior à entrada.");
            return;
        }

        int horas = calcularHoras(selecionado.getEntrada(), dataSaida);
        double valor = horas * obterValorHora(selecionado.getTipo());

        selecionado.setSaida(dataSaida);
        selecionado.setHoras(horas);
        selecionado.setValor(valor);
        selecionado.setSituacao("Finalizado");

        dgvVeiculos.refresh();
        atualizarIndicadores();
    }

    // Passo 10 - Pesquisar placa
    @FXML
    private void btnPesquisarClick() {
        String pesquisa = txtPesquisarPlaca.getText().trim().toUpperCase();
        dgvVeiculos.getSelectionModel().clearSelection();

        dadosFiltrados.setPredicate(p -> {
            if (pesquisa.isEmpty()) return true;
            return p.getPlaca().contains(pesquisa);
        });
    }

    @FXML
    private void btnMostrarTodosClick() {
        txtPesquisarPlaca.clear();
        dadosFiltrados.setPredicate(p -> true);
    }

    // Passo 11 - Remover Registro
    @FXML
    private void btnRemoverClick() {
        Permanencia selecionado = obterLinhaSelecionada();
        if (selecionado == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText("Deseja remover o registro selecionado?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            listaVeiculos.remove(selecionado);
            atualizarIndicadores();
        }
    }

    // Passo 12 - Atualizar Indicadores
    private void atualizarIndicadores() {
        long estacionados = listaVeiculos.stream().filter(v -> v.getSituacao().equals("Estacionado")).count();
        long finalizados = listaVeiculos.stream().filter(v -> v.getSituacao().equals("Finalizado")).count();
        double total = listaVeiculos.stream().filter(v -> v.getSituacao().equals("Finalizado")).mapToDouble(Permanencia::getValor).sum();

        lblEstacionados.setText(String.valueOf(estacionados));
        lblFinalizados.setText(String.valueOf(finalizados));
        lblTotalRecebido.setText(String.format("R$ %.2f", total));
    }

    // Passo 13 - Limpar Campos
    @FXML
    private void btnLimparClick() {
        limparCampos();
    }

    private void limparCampos() {
        txtPlaca.clear();
        txtModelo.clear();
        cboTipoVeiculo.getSelectionModel().clearSelection();
        resetarDatas();
        txtPlaca.requestFocus();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}