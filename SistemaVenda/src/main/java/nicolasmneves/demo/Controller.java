package nicolasmneves.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    // Componentes mapeados do FXML
    @FXML
    private TextField txtCliente;

    @FXML
    private TextField txtProduto;

    @FXML
    private TextField txtValorUnitario;

    @FXML
    private Spinner<Integer> nudQuantidade;

    // RadioButtons (Formas de Pagamento)
    @FXML
    private RadioButton radDinheiro;

    @FXML
    private RadioButton radPix;

    @FXML
    private RadioButton radDebito;

    @FXML
    private RadioButton radCredito;

    // CheckBoxes (Serviços Adicionais)
    @FXML
    private CheckBox chkGarantia;

    @FXML
    private CheckBox chkEntrega;

    // Grupo para garantir que apenas um RadioButton seja selecionado
    private ToggleGroup grupoPagamento;

    // Constantes de regras de negócio
    private static final double DESCONTO_DINHEIRO = 0.10;
    private static final double DESCONTO_PIX = 0.08;
    private static final double DESCONTO_DEBITO = 0.03;
    private static final double DESCONTO_CREDITO = 0.00;

    private static final double LIMITE_COMPRA_EXTRA = 1000.0;
    private static final double DESCONTO_EXTRA = 0.02;
    private static final double DESCONTO_MAXIMO = 0.15;

    private static final double VALOR_ENTREGA = 25.0;
    private static final double PERCENTUAL_GARANTIA = 0.05;

    @FXML
    public void initialize() {
        // Inicializa o Spinner para aceitar apenas números (mínimo 1, máximo 100, padrão 1)
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        nudQuantidade.setValueFactory(valueFactory);

        // Agrupa os RadioButtons logicamente (garante que apenas um fique marcado)
        grupoPagamento = new ToggleGroup();
        radDinheiro.setToggleGroup(grupoPagamento);
        radPix.setToggleGroup(grupoPagamento);
        radDebito.setToggleGroup(grupoPagamento);
        radCredito.setToggleGroup(grupoPagamento);
    }

    @FXML
    void handleCalcular(ActionEvent event) {
        try {
            // 1. Captura os dados básicos
            String cliente = txtCliente.getText();
            String produto = txtProduto.getText();
            double valorUnitario = Double.parseDouble(txtValorUnitario.getText().replace(",", "."));
            int quantidade = nudQuantidade.getValue();

            // Regra: a quantidade deverá ser maior que zero
            if (quantidade <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Quantidade Inválida");
                alert.setHeaderText("Quantidade deve ser maior que zero");
                alert.setContentText("Por favor, informe uma quantidade válida.");
                alert.showAndWait();
                return;
            }

            // 2. Calcula o subtotal (valor base, sem descontos ou acréscimos)
            double subtotal = valorUnitario * quantidade;

            // 3. Verifica a forma de pagamento selecionada e o desconto correspondente
            RadioButton selecionado = (RadioButton) grupoPagamento.getSelectedToggle();
            String formaPagamento = "Não informada";
            double percentualDesconto = 0.0;

            if (selecionado != null) {
                formaPagamento = selecionado.getText();

                if (selecionado == radDinheiro) {
                    percentualDesconto = DESCONTO_DINHEIRO;      // 10%
                } else if (selecionado == radPix) {
                    percentualDesconto = DESCONTO_PIX;           // 8%
                } else if (selecionado == radDebito) {
                    percentualDesconto = DESCONTO_DEBITO;        // 3%
                } else if (selecionado == radCredito) {
                    percentualDesconto = DESCONTO_CREDITO;       // 0%
                }
            }

            // Regra: compras acima de R$ 1.000,00 recebem mais 2% de desconto
            if (subtotal > LIMITE_COMPRA_EXTRA) {
                percentualDesconto += DESCONTO_EXTRA;
            }

            // Regra: o desconto total não poderá ultrapassar 15% do subtotal
            if (percentualDesconto > DESCONTO_MAXIMO) {
                percentualDesconto = DESCONTO_MAXIMO;
            }

            double valorDesconto = subtotal * percentualDesconto;
            double valorTotal = subtotal - valorDesconto;

            // 4. Verifica serviços adicionais (Garantia / Entrega)
            boolean querGarantia = chkGarantia.isSelected();
            boolean querEntrega = chkEntrega.isSelected();

            double valorGarantia = 0.0;
            if (querGarantia) {
                // Garantia estendida: 5% do subtotal
                valorGarantia = subtotal * PERCENTUAL_GARANTIA;
                valorTotal += valorGarantia;
            }

            double valorEntrega = 0.0;
            if (querEntrega) {
                // Entrega: taxa fixa de R$ 25,00
                valorEntrega = VALOR_ENTREGA;
                valorTotal += valorEntrega;
            }

            // 5. Exibe o resultado (Substitua por um Label na tela ou use um Alert)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resumo da Venda");
            alert.setHeaderText("Venda processada com sucesso!");
            alert.setContentText(String.format(
                    "Cliente: %s\nProduto: %s (x%d)\n" +
                            "Subtotal: R$ %.2f\n" +
                            "Pagamento: %s (desconto de %.0f%%: -R$ %.2f)\n" +
                            "Garantia estendida: %s%s\n" +
                            "Entrega: %s%s\n\n" +
                            "Total Geral: R$ %.2f",
                    cliente, produto, quantidade,
                    subtotal,
                    formaPagamento, percentualDesconto * 100, valorDesconto,
                    (querGarantia ? "Sim" : "Não"), (querGarantia ? String.format(" (+R$ %.2f)", valorGarantia) : ""),
                    (querEntrega ? "Sim" : "Não"), (querEntrega ? String.format(" (+R$ %.2f)", valorEntrega) : ""),
                    valorTotal
            ));
            alert.showAndWait();

        } catch (NumberFormatException e) {
            // Tratamento caso o usuário digite texto no campo de valor unitário
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Entrada");
            alert.setHeaderText("Valor Unitário Inválido");
            alert.setContentText("Por favor, insira um valor numérico válido no campo de preço.");
            alert.showAndWait();
        }
    }
}