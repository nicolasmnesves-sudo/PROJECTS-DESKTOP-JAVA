package nicolasmneves.boletimescolar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller da tela "Boletim Escolar".
 *
 * Regras de negócio:
 *  - Frequência é avaliada ANTES da média (prioridade sobre a nota).
 *  - Frequência < 75%                              -> "Reprovado por frequência"
 *  - Frequência adequada e média >= 7               -> "Aprovado"
 *  - Frequência adequada e média entre 5 e 6.99      -> "Recuperação"
 *  - Frequência adequada e média < 5                 -> "Reprovado por nota"
 */
public class BoletimEscolarController implements Initializable {

    private static final double FREQUENCIA_MINIMA = 75.0;
    private static final double MEDIA_APROVACAO = 7.0;
    private static final double MEDIA_RECUPERACAO = 5.0;

    @FXML private TextField nomeField;
    @FXML private TextField turmaField;
    @FXML private TextField nota1Field;
    @FXML private TextField nota2Field;
    @FXML private TextField nota3Field;
    @FXML private TextField nota4Field;
    @FXML private Spinner<Integer> frequenciaSpinner;

    @FXML private TextArea resultadoArea; // exibe o resultado do cálculo individual
    @FXML private TextArea listaArea;     // exibe a lista de registros já adicionados

    @FXML private Button calcularButton;
    @FXML private Button adicionarButton;
    @FXML private Button removerButton;
    @FXML private Button limparButton;
    @FXML private Button resumoButton;

    // ----- Estado interno -----
    private final List<Registro> registros = new ArrayList<>();
    private boolean resultadoCalculado = false;

    // dados do último cálculo, usados ao adicionar
    private double mediaCalculada;
    private String situacaoCalculada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        frequenciaSpinner.setValueFactory(valueFactory);
        frequenciaSpinner.setEditable(true);

        resultadoArea.setEditable(false);
        listaArea.setEditable(false);
    }

    // ---------------------------------------------------------------
    // Botão: Calcular
    // ---------------------------------------------------------------
    @FXML
    private void handleCalcular() {
        String nome = nomeField.getText() == null ? "" : nomeField.getText().trim();
        String turma = turmaField.getText() == null ? "" : turmaField.getText().trim();

        if (nome.isEmpty() || turma.isEmpty()) {
            mostrarErro("Informe o nome e a turma do aluno.");
            resultadoCalculado = false;
            return;
        }

        Double n1 = lerNota(nota1Field.getText());
        Double n2 = lerNota(nota2Field.getText());
        Double n3 = lerNota(nota3Field.getText());
        Double n4 = lerNota(nota4Field.getText());

        if (n1 == null || n2 == null || n3 == null || n4 == null) {
            mostrarErro("Todas as notas devem ser números válidos entre 0 e 10.");
            resultadoCalculado = false;
            return;
        }

        int frequencia = frequenciaSpinner.getValue();
        if (frequencia < 0 || frequencia > 100) {
            mostrarErro("A frequência deve estar entre 0 e 100.");
            resultadoCalculado = false;
            return;
        }

        double media = (n1 + n2 + n3 + n4) / 4.0;
        String situacao = calcularSituacao(media, frequencia);

        mediaCalculada = media;
        situacaoCalculada = situacao;
        resultadoCalculado = true;

        resultadoArea.setText(String.format(Locale.US,
                "Média: %.2f%nFrequência: %d%%%nSituação: %s",
                media, frequencia, situacao));
    }

    /**
     * Aplica a regra de prioridade: frequência é checada antes da média.
     */
    private String calcularSituacao(double media, int frequencia) {
        if (frequencia < FREQUENCIA_MINIMA) {
            return "Reprovado por frequência";
        }
        if (media >= MEDIA_APROVACAO) {
            return "Aprovado";
        }
        if (media >= MEDIA_RECUPERACAO) {
            return "Recuperação";
        }
        return "Reprovado por nota";
    }

    private Double lerNota(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            double valor = Double.parseDouble(texto.trim().replace(",", "."));
            if (valor < 0 || valor > 10) {
                return null;
            }
            return valor;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ---------------------------------------------------------------
    // Botão: Adicionar Resultado
    // ---------------------------------------------------------------
    @FXML
    private void handleAdicionar() {
        if (!resultadoCalculado) {
            mostrarErro("Calcule o resultado antes de adicionar.");
            return;
        }

        String nome = nomeField.getText().trim();
        String turma = turmaField.getText().trim();
        int frequencia = frequenciaSpinner.getValue();

        Registro registro = new Registro(nome, turma, mediaCalculada, frequencia, situacaoCalculada);
        registros.add(registro);

        atualizarListaArea();
        limparCampos();
        resultadoCalculado = false;
        resultadoArea.clear();
    }

    // ---------------------------------------------------------------
    // Botão: Remover
    // ---------------------------------------------------------------
    @FXML
    private void handleRemover() {
        if (registros.isEmpty()) {
            mostrarErro("Não há registros para remover.");
            return;
        }

        String selecionado = listaArea.getSelectedText();
        if (selecionado == null || selecionado.trim().isEmpty()) {
            mostrarErro("Selecione uma linha da lista para remover.");
            return;
        }

        String linhaSelecionada = selecionado.trim();
        boolean removido = registros.removeIf(r -> r.toString().equals(linhaSelecionada));

        if (!removido) {
            mostrarErro("Selecione a linha completa do registro que deseja remover.");
            return;
        }

        atualizarListaArea();
    }

    // ---------------------------------------------------------------
    // Botão: Limpar (lista) — com confirmação
    // ---------------------------------------------------------------
    @FXML
    private void handleLimpar() {
        if (registros.isEmpty()) {
            mostrarErro("A lista já está vazia.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente apagar todos os registros da lista?");

        confirmacao.showAndWait().ifPresent(botao -> {
            if (botao == ButtonType.OK) {
                registros.clear();
                atualizarListaArea();
            }
        });
    }

    // ---------------------------------------------------------------
    // Botão: Gerar Resumo
    // ---------------------------------------------------------------
    @FXML
    private void handleGerarResumo() {
        int total = registros.size();
        long aprovados = registros.stream().filter(r -> r.situacao.equals("Aprovado")).count();
        long recuperacao = registros.stream().filter(r -> r.situacao.equals("Recuperação")).count();
        long reprovadosNota = registros.stream().filter(r -> r.situacao.equals("Reprovado por nota")).count();
        long reprovadosFrequencia = registros.stream().filter(r -> r.situacao.equals("Reprovado por frequência")).count();

        String resumo = String.format(
                "Total de estudantes: %d%nAprovados: %d%nRecuperação: %d%nReprovados por nota: %d%nReprovados por frequência: %d",
                total, aprovados, recuperacao, reprovadosNota, reprovadosFrequencia);

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Resumo da turma");
        info.setHeaderText(null);
        info.setContentText(resumo);
        info.showAndWait();
    }

    // ---------------------------------------------------------------
    // Auxiliares
    // ---------------------------------------------------------------
    private void limparCampos() {
        nomeField.clear();
        turmaField.clear();
        nota1Field.clear();
        nota2Field.clear();
        nota3Field.clear();
        nota4Field.clear();
        frequenciaSpinner.getValueFactory().setValue(0);
    }

    private void atualizarListaArea() {
        StringBuilder sb = new StringBuilder();
        for (Registro r : registros) {
            sb.append(r.toString()).append(System.lineSeparator());
        }
        listaArea.setText(sb.toString());
    }

    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    /**
     * Representa um registro de aluno já adicionado à lista.
     */
    private static class Registro {
        final String nome;
        final String turma;
        final double media;
        final int frequencia;
        final String situacao;

        Registro(String nome, String turma, double media, int frequencia, String situacao) {
            this.nome = nome;
            this.turma = turma;
            this.media = media;
            this.frequencia = frequencia;
            this.situacao = situacao;
        }

        @Override
        public String toString() {
            return String.format(Locale.US,
                    "Aluno: %s | Turma: %s | Média: %.2f | Frequência: %d%% | Situação: %s",
                    nome, turma, media, frequencia, situacao);
        }
    }
}

