package nicolasmneves.sistemadecontroletanqueindustrial;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TanqueController {

    // ---------- Leituras ----------
    @FXML private TextField txtOperador;
    @FXML private TextField txtTemperatura;
    @FXML private TextField txtNivel;
    @FXML private Button btnProcessar;
    @FXML private Button btnLimpar;
    @FXML private Button btnSair;
    @FXML private Label lblCondicao;

    // ---------- Acionamentos ----------
    @FXML private Button btnLigarBomba;
    @FXML private Button btnDesligarBomba;
    @FXML private Label lblStatusBomba;

    @FXML private Button btnLigarVentilador;
    @FXML private Button btnDesligarVentilador;
    @FXML private Label lblStatusVentilador;

    @FXML private Button btnAbrirValvula;
    @FXML private Button btnFecharValvula;
    @FXML private Label lblStatusValvula;

    @FXML private Button btnEmergencia;

    // ---------- Registro de eventos ----------
    @FXML private ListView<String> lstEventos;

    // ----------  estados do processo ----------
    private boolean bombaLigada = false;
    private boolean ventiladorLigado = false;
    private boolean valvulaAberta = false;

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Chamado automaticamente pelo JavaFX após o carregamento do FXML.
     * Equivale ao construtor do MainForm no projeto em C#.
     */
    @FXML
    public void initialize() {
        AtualizarIndicadores();
        RegistrarEvento("Simulação iniciada.");
    }

    // ============================================================
    //  Etapa 2 - Registro de eventos
    // ============================================================
    private void RegistrarEvento(String mensagem) {
        String horario = LocalTime.now().format(FORMATO_HORA);
        lstEventos.getItems().add(0, horario + " - " + mensagem);
    }

    // ============================================================
    //  Etapa 3 - Atualizar indicadores visuais
    // ============================================================
    private void AtualizarIndicadores() {
        lblStatusBomba.setText(bombaLigada ? "BOMBA: LIGADA" : "BOMBA: DESLIGADA");
        lblStatusBomba.getStyleClass().removeAll("status-off", "status-on");
        lblStatusBomba.getStyleClass().add(bombaLigada ? "status-on" : "status-off");

        lblStatusVentilador.setText(ventiladorLigado ? "VENTILADOR: LIGADO" : "VENTILADOR: DESLIGADO");
        lblStatusVentilador.getStyleClass().removeAll("status-off", "status-on");
        lblStatusVentilador.getStyleClass().add(ventiladorLigado ? "status-on" : "status-off");

        lblStatusValvula.setText(valvulaAberta ? "VÁLVULA: ABERTA" : "VÁLVULA: FECHADA");
        lblStatusValvula.getStyleClass().removeAll("status-off", "status-open");
        lblStatusValvula.getStyleClass().add(valvulaAberta ? "status-open" : "status-off");
    }

    // ============================================================
    //  Etapa 4 - Validar as entradas (RN01 e RN02)
    // ============================================================

    /** Pequena estrutura auxiliar para simular o "out" do C# no LerEntradas. */
    private static class Leitura {
        double temperatura;
        double nivel;
    }

    private Leitura LerEntradas() {
        if (txtOperador.getText() == null || txtOperador.getText().trim().isEmpty()) {
            exibirAviso("Informe o nome do operador.");
            txtOperador.requestFocus();
            return null;
        }

        Double temperatura = tentarConverter(txtTemperatura.getText());
        if (temperatura == null) {
            exibirAviso("Digite uma temperatura numérica.");
            txtTemperatura.requestFocus();
            return null;
        }

        Double nivel = tentarConverter(txtNivel.getText());
        if (nivel == null) {
            exibirAviso("Digite um nível numérico.");
            txtNivel.requestFocus();
            return null;
        }

        if (temperatura < 0 || temperatura > 100) {
            exibirAviso("A temperatura deve estar entre 0 e 100 °C.");
            return null;
        }
        if (nivel < 0 || nivel > 100) {
            exibirAviso("O nível deve estar entre 0 e 100%.");
            return null;
        }

        Leitura leitura = new Leitura();
        leitura.temperatura = temperatura;
        leitura.nivel = nivel;
        return leitura;
    }

    /** Equivalente ao double.TryParse do C#: devolve null em caso de falha. */
    private Double tentarConverter(String texto) {
        if (texto == null) return null;
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ============================================================
    //  - Aplicar as regras automáticas (RN03 a RN08)
    // ============================================================
    private void AplicarRegrasAutomaticas(double temperatura, double nivel) {
        // ---- Temperatura / ventilador (com histerese didática) ----
        if (temperatura >= 70) {
            ventiladorLigado = true;
            lblCondicao.setText("ATENÇÃO: temperatura alta.");
            lblCondicao.setStyle("-fx-background-color: #ffb74d;");
        } else if (temperatura < 65) {
            ventiladorLigado = false;
            lblCondicao.setText("Temperatura em faixa normal.");
            lblCondicao.setStyle("-fx-background-color: #b6f0c2;");
        } else {
            lblCondicao.setText("Faixa de transição: estado mantido.");
            lblCondicao.setStyle("-fx-background-color: #f0e191;");
        }

        // ---- Nível / bomba / válvula ----
        if (nivel <= 20) {
            bombaLigada = true;
            valvulaAberta = false;
            RegistrarEvento("Nível baixo: bomba ligada e válvula fechada.");
        } else if (nivel >= 90) {
            bombaLigada = false;
            valvulaAberta = true;
            RegistrarEvento("Nível alto: bomba desligada e válvula aberta.");
        }
        // entre 20% e 90% (RN08): mantém bomba e válvula sem alteração automática

        AtualizarIndicadores();
    }

    // ============================================================
    //  - Processar leituras
    // ============================================================
    @FXML
    private void onProcessar() {
        Leitura leitura = LerEntradas();
        if (leitura == null) {
            return;
        }

        AplicarRegrasAutomaticas(leitura.temperatura, leitura.nivel);

        RegistrarEvento(
                "Operador " + txtOperador.getText() +
                        " processou T=" + formatarNumero(leitura.temperatura) +
                        " °C e N=" + formatarNumero(leitura.nivel) + "%."
        );
    }

    // ============================================================
    //  - Acionamentos manuais (com intertravamento)
    // ============================================================
    @FXML
    private void onLigarBomba() {
        if (valvulaAberta) {
            exibirAviso("Feche a válvula antes de ligar a bomba.");
            return;
        }
        bombaLigada = true;
        AtualizarIndicadores();
        RegistrarEvento("Bomba ligada manualmente.");
    }

    @FXML
    private void onDesligarBomba() {
        bombaLigada = false;
        AtualizarIndicadores();
        RegistrarEvento("Bomba desligada manualmente.");
    }

    @FXML
    private void onLigarVentilador() {
        ventiladorLigado = true;
        AtualizarIndicadores();
        RegistrarEvento("Ventilador ligado manualmente.");
    }

    @FXML
    private void onDesligarVentilador() {
        ventiladorLigado = false;
        AtualizarIndicadores();
        RegistrarEvento("Ventilador desligado manualmente.");
    }

    @FXML
    private void onAbrirValvula() {
        if (bombaLigada) {
            exibirAviso("Desligue a bomba antes de abrir a válvula.");
            return;
        }
        valvulaAberta = true;
        AtualizarIndicadores();
        RegistrarEvento("Válvula aberta manualmente.");
    }

    @FXML
    private void onFecharValvula() {
        valvulaAberta = false;
        AtualizarIndicadores();
        RegistrarEvento("Válvula fechada manualmente.");
    }

    // ============================================================
    //   - Emergência, limpeza e saída
    // ============================================================
    @FXML
    private void onEmergencia() {
        bombaLigada = false;
        ventiladorLigado = false;
        valvulaAberta = false;

        lblCondicao.setText("PARADA DE EMERGÊNCIA ACIONADA");
        lblCondicao.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");

        AtualizarIndicadores();
        RegistrarEvento("Parada de emergência acionada.");
    }

    @FXML
    private void onLimpar() {
        txtOperador.clear();
        txtTemperatura.clear();
        txtNivel.clear();

        lblCondicao.setText("Aguardando leituras...");
        lblCondicao.setStyle("-fx-background-color: #eef1f4; -fx-text-fill: black;");

        txtOperador.requestFocus();
    }

    @FXML
    private void onSair() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja encerrar a simulação?", ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText(null);

        confirmacao.showAndWait().ifPresent(botao -> {
            if (botao == ButtonType.YES) {
                Stage stage = (Stage) btnSair.getScene().getWindow();
                stage.close();
                Platform.exit();
            }
        });
    }

    // ============================================================
    //  Auxiliares
    // ============================================================
    private void exibirAviso(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.WARNING, mensagem, ButtonType.OK);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    private String formatarNumero(double valor) {
        if (valor == Math.floor(valor)) {
            return String.valueOf((long) valor);
        }
        return String.format(Locale.forLanguageTag("pt-BR"), "%.1f", valor);
    }
}
