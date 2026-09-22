package nicolasmneves.telaequacaosegundograu;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    //Atributos =============
    @FXML
    private TextField numA;

    @FXML
    private TextField numB;

    @FXML
    private TextField numC;

    @FXML
    private Label lblResultado;

    @FXML
    protected void onButtonClick() {
                try {

                    int a = Integer.parseInt(numA.getText());
                    int b = Integer.parseInt(numB.getText());
                    int c = Integer.parseInt(numC.getText());


                    nicolasmneves.telaequacaosegundograu.Equacao equacao = new nicolasmneves.telaequacaosegundograu.Equacao(a, b, c);


                    if (!equacao.eValida()) {
                        lblResultado.setText("O valor de 'a' não pode ser zero. Não é uma equação do segundo grau.");
                        return;
                    }

                    double delta = equacao.calcularDelta();


                    if (delta < 0) {
                        lblResultado.setText(String.format("Delta = %.2f\nNão há raízes reais, pois o delta é negativo.", delta));
                    } else if (delta == 0) {
                        double x = equacao.calcularX1();
                        lblResultado.setText(String.format("Delta = 0\nA equação possui uma única raiz real:\nx = %.2f", x));
                    } else {
                        double x1 = equacao.calcularX1();
                        double x2 = equacao.calcularX2();
                        lblResultado.setText(String.format("Delta = %.2f\nA equação possui duas raízes reais:\nx1 = %.2f\nx2 = %.2f", delta, x1, x2));
                    }

                } catch (NumberFormatException e) {
                    lblResultado.setText("Por favor, insira valores inteiros válidos.");

                }
            }
        }
