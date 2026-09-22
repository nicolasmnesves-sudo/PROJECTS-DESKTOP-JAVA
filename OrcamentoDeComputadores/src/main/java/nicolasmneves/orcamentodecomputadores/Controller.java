package nicolasmneves.orcamentodecomputadores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.text.NumberFormat;
import java.util.Locale;

public class Controller {

    // Vincular os componentes do FXML usando as IDs corretas
    @FXML private TextField txtCliente; // Adicione fx:id="txtCliente" no FXML
    @FXML private ComboBox<String> cbPerfil;
    @FXML private ComboBox<String> cbPlacaMae;
    @FXML private ComboBox<String> cbProcessador;
    @FXML private ComboBox<String> cbPlacaVideo;
    @FXML private ComboBox<String> cbMemoriaRam;
    @FXML private ComboBox<String> cbArmazenamento;
    @FXML private ComboBox<String> cbFonte;
    @FXML private ComboBox<String> cbGabinete;
    @FXML private CheckBox chkRecomendado; // Adicione fx:id="chkRecomendado" no FXML

    // Opcionais (Adicionar no FXML se necessário, ou usar variáveis lógicas)
    private final NumberFormat moedaFormato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        // Carrega as opções nos ComboBoxes assim que a tela abre (Equivalente ao Form_Load)
        cbPerfil.getItems().addAll("Administrativo", "Professor de Tecnologia", "Desenvolvedor de Software", "Desenvolvedor de Jogos");
        cbProcessador.getItems().addAll("Básico", "Intermediário", "Avançado", "Alto desempenho");
        cbPlacaMae.getItems().addAll("Básica", "Intermediária", "Avançada");
        cbMemoriaRam.getItems().addAll("8 GB", "16 GB", "32 GB");
        cbArmazenamento.getItems().addAll("240 GB", "500 GB", "1 TB");
        cbPlacaVideo.getItems().addAll("Integrada", "Dedicada básica", "Dedicada intermediária");
        cbFonte.getItems().addAll("500 W", "650 W", "750 W");
        cbGabinete.getItems().addAll("Básico", "Intermediário", "Gamer");

        // Evento para quando o usuário marcar "Configuração recomendada"
        chkRecomendado.setOnAction(e -> {
            if (chkRecomendado.isSelected()) {
                carregarRecomendacao();
            }
        });
    }

    private void carregarRecomendacao() {
        String perfil = cbPerfil.getValue();
        if (perfil == null) {
            exibirAlerta("Aviso", "Selecione um Perfil primeiro para carregar a recomendação!");
            chkRecomendado.setSelected(false);
            return;
        }

        switch (perfil) {
            case "Administrativo":
                cbProcessador.setValue("Básico");
                cbPlacaMae.setValue("Básica");
                cbMemoriaRam.setValue("8 GB");
                cbArmazenamento.setValue("240 GB");
                cbPlacaVideo.setValue("Integrada");
                cbFonte.setValue("500 W");
                cbGabinete.setValue("Básico");
                break;
            case "Professor de Tecnologia":
                cbProcessador.setValue("Intermediário");
                cbPlacaMae.setValue("Intermediária");
                cbMemoriaRam.setValue("16 GB");
                cbArmazenamento.setValue("500 GB");
                cbPlacaVideo.setValue("Integrada");
                cbFonte.setValue("500 W");
                cbGabinete.setValue("Intermediário");
                break;
            case "Desenvolvedor de Software":
                cbProcessador.setValue("Avançado");
                cbPlacaMae.setValue("Intermediária");
                cbMemoriaRam.setValue("16 GB");
                cbArmazenamento.setValue("500 GB");
                cbPlacaVideo.setValue("Dedicada básica");
                cbFonte.setValue("650 W");
                cbGabinete.setValue("Intermediário");
                break;
            case "Desenvolvedor de Jogos":
                cbProcessador.setValue("Alto desempenho");
                cbPlacaMae.setValue("Avançada");
                cbMemoriaRam.setValue("32 GB");
                cbArmazenamento.setValue("1 TB");
                cbPlacaVideo.setValue("Dedicada intermediária");
                cbFonte.setValue("750 W");
                cbGabinete.setValue("Gamer");
                break;
        }
    }

    @FXML
    public void btnVerOrcamento_Click() {
        // 1. Validações Básicas
        if (txtCliente.getText() == null || txtCliente.getText().trim().isEmpty()) {
            exibirAlerta("Erro", "Por favor, informe o nome do cliente.");
            return;
        }
        if (cbPerfil.getValue() == null) {
            exibirAlerta("Erro", "Selecione o perfil do cliente.");
            return;
        }
        if (cbProcessador.getValue() == null || cbMemoriaRam.getValue() == null || cbArmazenamento.getValue() == null) {
            exibirAlerta("Erro", "Selecione todos os componentes essenciais.");
            return;
        }

        // 2. Cálculo dos Preços
        double subtotal = 0;

        // Processador
        if ("Básico".equals(cbProcessador.getValue())) subtotal += 650;
        else if ("Intermediário".equals(cbProcessador.getValue())) subtotal += 1000;
        else if ("Avançado".equals(cbProcessador.getValue())) subtotal += 1450;
        else if ("Alto desempenho".equals(cbProcessador.getValue())) subtotal += 2300;

        // Placa-mãe
        if ("Básica".equals(cbPlacaMae.getValue())) subtotal += 550;
        else if ("Intermediária".equals(cbPlacaMae.getValue())) subtotal += 750;
        else if ("Avançada".equals(cbPlacaMae.getValue())) subtotal += 1100;

        // Memória RAM
        if ("8 GB".equals(cbMemoriaRam.getValue())) subtotal += 180;
        else if ("16 GB".equals(cbMemoriaRam.getValue())) subtotal += 320;
        else if ("32 GB".equals(cbMemoriaRam.getValue())) subtotal += 620;

        // Armazenamento
        if ("240 GB".equals(cbArmazenamento.getValue())) subtotal += 180;
        else if ("500 GB".equals(cbArmazenamento.getValue())) subtotal += 280;
        else if ("1 TB".equals(cbArmazenamento.getValue())) subtotal += 480;

        // Placa de Vídeo
        if ("Dedicada básica".equals(cbPlacaVideo.getValue())) subtotal += 1500;
        else if ("Dedicada intermediária".equals(cbPlacaVideo.getValue())) subtotal += 2300;

        // Fonte
        if ("500 W".equals(cbFonte.getValue())) subtotal += 280;
        else if ("650 W".equals(cbFonte.getValue())) subtotal += 420;
        else if ("750 W".equals(cbFonte.getValue())) subtotal += 580;

        // Gabinete
        if ("Básico".equals(cbGabinete.getValue())) subtotal += 250;
        else if ("Intermediário".equals(cbGabinete.getValue())) subtotal += 350;
        else if ("Gamer".equals(cbGabinete.getValue())) subtotal += 500;

        // 3. Taxas padrão (Conforme enunciado, taxa de 10% e sem desconto inicial)
        double taxaMontagem = 10.0;
        double valorMontagem = subtotal * (taxaMontagem / 100);
        double totalFinal = subtotal + valorMontagem;

        // 4. Lógica de Compatibilidade
        String perfil = cbPerfil.getValue();
        String situacao = "Configuração adequada.";
        StringBuilder criticas = new StringBuilder();

        if ("Desenvolvedor de Jogos".equals(perfil)) {
            if (!"32 GB".equals(cbMemoriaRam.getValue())) criticas.append("- Memória mínima necessária: 32 GB.\n");
            if (!"1 TB".equals(cbArmazenamento.getValue())) criticas.append("- Armazenamento mínimo: 1 TB.\n");
            if ("Integrada".equals(cbPlacaVideo.getValue())) criticas.append("- Placa de vídeo dedicada obrigatória.\n");
            if ("Básico".equals(cbProcessador.getValue()) || "Intermediário".equals(cbProcessador.getValue())) criticas.append("- Processador Avançado/Alto Desempenho obrigatório.\n");
        }
        else if ("Desenvolvedor de Software".equals(perfil)) {
            if ("Integrada".equals(cbPlacaVideo.getValue())) {
                situacao = "Adequada com ressalva.";
                criticas.append("- É recomendada uma placa de vídeo dedicada para este perfil.\n");
            }
        }

        if (criticas.length() > 0 && !"Adequada com ressalva.".equals(situacao)) {
            situacao = "Configuração incompatível.";
        }

        // 5. Exibir Resultado Final em uma Caixa de Diálogo
        String relatorio = "Cliente: " + txtCliente.getText() + "\n" +
                "Perfil: " + perfil + "\n" +
                "---------------------------\n" +
                "Subtotal: " + moedaFormato.format(subtotal) + "\n" +
                "Taxa de Montagem (10%): " + moedaFormato.format(valorMontagem) + "\n" +
                "Total Final: " + moedaFormato.format(totalFinal) + "\n" +
                "---------------------------\n" +
                "Situação: " + situacao + "\n" +
                (criticas.length() > 0 ? "\nAlertas Técnicos:\n" + criticas.toString() : "Orçamento gerado com sucesso.");

        exibirRelatorio(situacao, relatorio);
    }

    private void exibirAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void exibirRelatorio(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText("Resumo do Orçamento");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}