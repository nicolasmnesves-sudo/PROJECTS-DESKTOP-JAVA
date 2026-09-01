package nicolasmneves.telaequacaosegundograu;

public class Equacao {
    // Atributos privados (apenas os números, sem componentes de tela)
    private final int a;
    private final int b;
    private final int c;

    // Construtor: Recebe os números já convertidos do Controller
    public Equacao(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    // Métodos de cálculo
    public boolean eValida() {
        return this.a != 0;
    }

    public double calcularDelta() {
        return (this.b * this.b) - (4 * this.a * this.c);
    }

    public double calcularX1() {
        return (-this.b + Math.sqrt(calcularDelta())) / (2.0 * this.a);
    }

    public double calcularX2() {
        return (-this.b - Math.sqrt(calcularDelta())) / (2.0 * this.a);
    }
}